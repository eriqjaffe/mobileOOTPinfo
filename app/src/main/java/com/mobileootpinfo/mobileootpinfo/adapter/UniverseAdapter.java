package com.mobileootpinfo.mobileootpinfo.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.fragments.ManageUniverses;
import com.mobileootpinfo.mobileootpinfo.model.Universe;
import com.mobileootpinfo.mobileootpinfo.model.UniverseLeague;
import com.mobileootpinfo.mobileootpinfo.util.AlphaNumericInputFilter;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.mobileootpinfo.mobileootpinfo.util.ValidateCSVPath;
import com.mobileootpinfo.mobileootpinfo.util.ValidateHTMLPath;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eriqj on 2/14/2018.
 */

public class UniverseAdapter extends ArrayAdapter<Universe> implements Validator.ValidationListener {

    private Context mContext;
    private List<Universe> universeList = new ArrayList<>();
    private Fragment fragment;
    private boolean namChanged = false;
    private Universe universe;

    protected Validator validator;
    protected boolean validated;

    private TextView id, name;
    private CheckBox defaultUniverse;
    private boolean wasDefaultUniverse;
    private String originalName;
    private Map<String, Integer> displayMap;
    private Spinner spinner;
    private DatabaseHandler db;

    private int chosenUniverseID;

    private String fragmentTag;

    @NotEmpty(messageResId = R.string.universe_must_have_name)
    private EditText universeName;

    @NotEmpty(sequence = 1, messageResId = R.string.csv_path_required)
    @ValidateCSVPath(sequence = 2, messageResId = R.string.invalid_csv_path, url = "")
    private EditText universeCSV;

    @ValidateHTMLPath(messageResId = R.string.invalid_html_path, url = "")
    private EditText universeHTML;

    private AlertDialog dialog;

    public UniverseAdapter(@NonNull Context context, List<Universe> list) {
        super(context, 0, list);
        mContext = context;
        universeList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        validator = new Validator(this);
        validator.setValidationListener(this);

        Validator.registerAnnotation(ValidateCSVPath.class);
        Validator.registerAnnotation(ValidateHTMLPath.class);

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.league_list_layout, parent, false);

        final Universe currentUniverse = universeList.get(position);

        db = new DatabaseHandler(mContext);

        id = listItem.findViewById(R.id.leagueID);
        id.setText(""+currentUniverse.getID());

        name = listItem.findViewById(R.id.leagueName);
        if (currentUniverse.getDefaultUniverse()) {
            name.setText("*"+currentUniverse.getName());
        } else {
            name.setText(currentUniverse.getName());
        }

        TextView defaultLeague = listItem.findViewById(R.id.defaultLeague);
        defaultLeague.setText(String.valueOf(currentUniverse.getDefaultLeague()));

        ImageButton delete = listItem.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete "+currentUniverse.getName()+"?");
                builder
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                        preDeleteWrapper w = new preDeleteWrapper();
                                        w.universe = currentUniverse;
                                        w.view = view;
                                        new deleteUniverse().execute(w);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ImageButton go = listItem.findViewById(R.id.chooseButton);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NavDrawer.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("html_root", currentUniverse.getHTML());
                mBundle.putString("universe_name", currentUniverse.getName());
                mBundle.putInt("universe_id", currentUniverse.getID());
                mBundle.putInt("league_id", currentUniverse.getDefaultLeague());
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        });

        ImageButton edit = listItem.findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenUniverseID = currentUniverse.getID();
                final Universe universe = db.getAppUniverse(currentUniverse.getID());
                LayoutInflater li = LayoutInflater.from(v.getContext());
                final View editView = li.inflate(R.layout.universe_edit_prompt, null);

                universeName = editView.findViewById(R.id.universeName);
                universeName.setText(universe.getName());
                ArrayList<InputFilter> curInputFilters = new ArrayList<InputFilter>(Arrays.asList(universeName.getFilters()));
                curInputFilters.add(0, new AlphaNumericInputFilter());
                InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
                universeName.setFilters(newInputFilters);

                originalName = universe.getName();

                universeCSV = editView.findViewById(R.id.universeCSV);
                universeCSV.setText(universe.getCSV());

                universeHTML = editView.findViewById(R.id.universeHTML);
                universeHTML.setText(universe.getHTML());

                //validator.put(universeName, new CheckIfUniverseExists(2));

                defaultUniverse = editView.findViewById(R.id.defaultUniverse);
                defaultUniverse.setChecked(universe.getDefaultUniverse());
                wasDefaultUniverse = universe.getDefaultUniverse();

                final List<UniverseLeague> leagueMap = db.getUniverseLeagues(universe);
                displayMap = new HashMap<>();
                final Map<Integer, String> selectMap = new HashMap<>();
                for(UniverseLeague uL : leagueMap){
                    displayMap.put(uL.getLeagueName(), uL.getLeagueID());
                    selectMap.put(uL.getLeagueID(), uL.getLeagueName());
                }
                spinner = editView.findViewById(R.id.defaultLeagueSpinner);
                List<String> leagueList = new ArrayList<String>(displayMap.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, leagueList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                int spinnerPosition = adapter.getPosition(selectMap.get(universe.getDefaultLeague()));
                System.out.println(universe.getDefaultLeague() + " SELECTED LEAGUE" + spinnerPosition);
                spinner.setSelection(spinnerPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(editView);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //UpdateUniverse.cancel(true);
                                dialog.cancel();

                            }
                        });
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        List<String> otherNames = db.getOtherUniverseNames(originalName);
                        if (!otherNames.contains(universeName.getText().toString().trim())) {
                            validator.validate();
                        } else {
                            Snackbar mySnackbar = Snackbar.make(editView, "That universe name is already in use!", Snackbar.LENGTH_LONG);
                            View snackBarView = mySnackbar.getView();
                            snackBarView.setBackgroundColor(mContext.getResources().getColor(R.color.colorError));
                            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            mySnackbar.show();
                        }
                    }
                });
            }
        });
        return listItem;
    }

    @Override
    public int getCount() {
        return universeList.size();
    }

    /*private void reloadFragment(DatabaseHandler db) {
        universeList.clear();
        universeList = db.getAllAppUniverses();
        addAll(universeList);
        notifyDataSetChanged();
    }*/

    private class preDeleteWrapper {
        Universe universe;
        View view;
    }

    private class DeleteWrapper {
        Universe universe;
        boolean success;
        int universeCount;
        View view;
    }

    class deleteUniverse extends AsyncTask<preDeleteWrapper, Void, DeleteWrapper> {

        final DatabaseHandler db = new DatabaseHandler(mContext);
        final LayoutInflater li = LayoutInflater.from(mContext);
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            final View downloadView = li.inflate(R.layout.get_leaders_progress, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(downloadView);
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected DeleteWrapper doInBackground(preDeleteWrapper... params) {
            preDeleteWrapper in = params[0];
            Universe currentUniverse = in.universe;
            View view = in.view;
            DeleteWrapper w = new DeleteWrapper();
            w.view = view;
            w.universe = currentUniverse;
            if (db.deleteUniverse(currentUniverse, getContext()) > 0) {
                w.universeCount = db.getUniverseCount();
                w.success = true;
            } else {
                w.success = false;
                Log.e("ADAPTER", "Something went wrong.");
            }
            return w;
        }

        protected void onPostExecute(DeleteWrapper w) {
            dialog.cancel();
            View view = w.view;
            if (w.success) {
                Universe currentUniverse = w.universe;
                remove(currentUniverse);
                final int universeCount = db.getUniverseCount();
                if (currentUniverse.getDefaultUniverse() && universeCount > 0) {
                    final View dialogView = li.inflate(R.layout.new_default_universe, null);
                    final Spinner universeSelect = dialogView.findViewById(R.id.universeSpinner);
                    final Universe chosenUniverse = new Universe();
                    ArrayList<UniverseSpinnerAdapter> spinnerUniverses = new ArrayList<>();
                    spinnerUniverses.clear();
                    List<Universe> universes = db.getAllAppUniverses();
                    for (Universe u : universes) {
                        spinnerUniverses.add(new UniverseSpinnerAdapter(u.getID(), u.getName(), u.getCSV(), u.getHTML()));
                    }
                    ArrayAdapter<UniverseSpinnerAdapter> adapter = new ArrayAdapter<UniverseSpinnerAdapter>(mContext, R.layout.spinner_item, spinnerUniverses);
                    universeSelect.setAdapter(adapter);
                    universeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            UniverseSpinnerAdapter universe = (UniverseSpinnerAdapter) parent.getSelectedItem();
                            chosenUniverse.setID(universe.getId());;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    AlertDialog dialog2 = new AlertDialog.Builder(mContext)
                            .setView(dialogView)
                            .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    db.setDefaultAppUniverse(chosenUniverse.getID());
                                    reloadFragment();
                                    /*universeList.clear();
                                    universeList = db.getAllAppUniverses();
                                    addAll(universeList);
                                    notifyDataSetChanged();*/
                                }
                            })
                            .create();

                    dialog2.show();
                } else {
                    reloadFragment();
                }
            } else {
                final Snackbar mySnackbar = Snackbar.make(view.findViewById(android.R.id.content), R.string.delete_universe_error, Snackbar.LENGTH_LONG);
                final View snackBarView = mySnackbar.getView();
                snackBarView.setBackgroundColor(mContext.getResources().getColor(R.color.colorError));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                mySnackbar.show();
            }

        }
    }

    private void reloadFragment() {
        android.support.v4.app.Fragment fragment = new ManageUniverses();
        fragmentTag = "nav_switch";
        FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.currentFrame, fragment, fragmentTag);
        ft.commit();
    }

    @Override
    public void onValidationSucceeded() {
        validated = true;
        final DatabaseHandler db = new DatabaseHandler(mContext);
        db.editUniverse(chosenUniverseID, universeName.getText().toString(), universeCSV.getText().toString(),
                universeHTML.getText().toString(), defaultUniverse.isChecked(), displayMap.get(spinner.getSelectedItem()),
                originalName, mContext);
        if (wasDefaultUniverse && !defaultUniverse.isChecked()) {
            if (!db.isThereADefaultUniverse()) {
                LayoutInflater li = LayoutInflater.from(getContext());
                final View promptsView = li.inflate(R.layout.default_league_prompt, null);
                final Spinner spinner = promptsView.findViewById(R.id.defaultLeagueSpinner);

                List<Universe> universes = db.getAllAppUniverses();
                final Map<String, Integer> universeDisplayMap = new HashMap<>();
                final Map<Integer, String> universeSelectMap = new HashMap<>();

                for(Universe u : universes){
                    universeDisplayMap.put(u.getName(), u.getID());
                    universeSelectMap.put(u.getID(), u.getName());
                }
                List<String> universeAdapterList = new ArrayList<String>(universeDisplayMap.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, universeAdapterList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(promptsView);
                builder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        db.setDefaultAppUniverse(universeDisplayMap.get(spinner.getSelectedItem()));
                                        reloadFragment();
                                        /*universeList.clear();
                                        universeList = db.getAllAppUniverses();
                                        addAll(universeList);
                                        notifyDataSetChanged();*/
                                    }
                                });
                AlertDialog dialog2 = builder.create();
                dialog2.show();
            }

        } else {
            reloadFragment();
            /*android.support.v4.app.Fragment fragment = new ManageUniverses();
            fragmentTag = "nav_switch";
            FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.currentFrame, fragment, fragmentTag);
            ft.commit();*/
        }
        dialog.cancel();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validated = false;

        for (ValidationError error : errors) {

            View   view    = error.getView();

            List Errors    = error.getFailedRules();
            Rule rule      = (Rule) Errors.get(0);
            String message = rule.getMessage( getContext() );

            if ( view instanceof TextView )
            {
                ((EditText) view).setError(message);
            }
        }
    }

    public class CheckIfUniverseExists extends QuickRule<EditText> {

        public CheckIfUniverseExists(int sequence) {
            super(sequence);
        }

        @Override
        public boolean isValid(EditText editText) {
            System.out.println("I GOT THIS FAR!");
            String universeName = editText.getText().toString().trim();
            List<String> universes = db.getOtherUniverseNames(universeName);
            if (universes.isEmpty() || !universes.contains(universeName)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getMessage(Context context) {
            return "Universe name already exists";
        }
    }
}
