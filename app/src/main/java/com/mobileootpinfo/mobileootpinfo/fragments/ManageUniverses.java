package com.mobileootpinfo.mobileootpinfo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobileootpinfo.mobileootpinfo.R;
import com.mobileootpinfo.mobileootpinfo.activity.NavDrawer;
import com.mobileootpinfo.mobileootpinfo.adapter.UniverseAdapter;
import com.mobileootpinfo.mobileootpinfo.model.Universe;
import com.mobileootpinfo.mobileootpinfo.util.AlphaNumericInputFilter;
import com.mobileootpinfo.mobileootpinfo.util.DatabaseHandler;
import com.mobileootpinfo.mobileootpinfo.util.ValidateCSVPath;
import com.mobileootpinfo.mobileootpinfo.util.ValidateHTMLPath;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

public class ManageUniverses extends Fragment implements Validator.ValidationListener {

    private View view;
    private UniverseAdapter universeAdapter;
    private String universeCSV, universeName, universeHTML;
    private boolean defaultUniverse;
    private FloatingActionButton fab;

    @NotEmpty(messageResId = R.string.universe_must_have_name)
    private EditText name;

    @NotEmpty(sequence = 1, messageResId = R.string.csv_path_required)
    @ValidateCSVPath(sequence = 2, messageResId = R.string.invalid_csv_path, url = "")
    private EditText csv;

    @ValidateHTMLPath(messageResId = R.string.invalid_html_path, url = "")
    private EditText html;

    private CheckBox defaultUniverseCB;
    private Spinner leagueSelect;
    private DatabaseHandler db;
    private List<String> existingUniverses;

    private AlertDialog dialog;

    protected Validator validator;
    protected boolean validated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.content_manage_universes, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHandler(getContext());

        validator = new Validator(this);
        validator.setValidationListener(this);

        Validator.registerAnnotation(ValidateCSVPath.class);
        Validator.registerAnnotation(ValidateHTMLPath.class);

        existingUniverses = populateListView(getContext());

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(view.getContext());
                final View promptsView = li.inflate(R.layout.universe_prompt, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(promptsView);
                builder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {}
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        name = promptsView.findViewById(R.id.universeName);
                        csv = promptsView.findViewById(R.id.universeCSV);
                        html = promptsView.findViewById(R.id.universeHTML);
                        universeName = name.getText().toString();
                        defaultUniverseCB = promptsView.findViewById(R.id.defaultUniverse);
                        universeCSV = csv.getText().toString();
                        if (!universeCSV.endsWith("/")) {
                            universeCSV += "/";
                        }
                        universeHTML = html.getText().toString();
                        if (!universeHTML.endsWith("/")) {
                            universeHTML += "/";
                        }
                        validator.put(name, new CheckIfUniverseExists(2));

                        //validator.validate(true);
                        validate();
                        //new verifyURL().execute(universeCSV+"leagues.csv", universeHTML+getString(R.string.index_html));

                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });

                EditText nameLine = promptsView.findViewById(R.id.universeName);
                ArrayList<InputFilter> curInputFilters = new ArrayList<InputFilter>(Arrays.asList(nameLine.getFilters()));
                curInputFilters.add(0, new AlphaNumericInputFilter());
                InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
                nameLine.setFilters(newInputFilters);
            }
        });

        ((NavDrawer) getActivity())
                .formatActionBar("Manage Universes", "* indicates default universe",
                        R.color.colorPrimary, Color.parseColor("#FFFFFF"));
    }

    public List<String> populateListView(Context ctx) {
        List<String> universeList = new ArrayList<String>();
        ListView listView = view.findViewById(R.id.universe_list_view);
        List<Universe> universes = db.getAllAppUniverses();
        for (Universe u : universes) {
            universeList.add(u.getName());
            //existingUniverses.add(u.getName());
        }
        universeAdapter = new UniverseAdapter(ctx, universes);
        listView.setAdapter(universeAdapter);
        return universeList;
    }

    public void clearListView() {
        ListView listView = view.findViewById(R.id.universe_list_view);
        universeAdapter.clear();
        listView.setAdapter(universeAdapter);
    }

    private class UniverseWrapper {
        String universeName;
        String universeCSV;
        String universeHTML;
        boolean defaultUniverse;
    }

    private class CheckWrapper2 {
        String universeName;
        String universeCSV;
        String universeHTML;
        boolean defaultUniverse;
        boolean valid;
        Map<Integer, String> leagueMap;
    }

    private class createUniverse extends AsyncTask<UniverseWrapper, Void, CheckWrapper2> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected CheckWrapper2 doInBackground(UniverseWrapper... params) {

            UniverseWrapper w = params[0];
            CheckWrapper2 cw = new CheckWrapper2();
            Map<Integer, String> LeagueList = new TreeMap<Integer, String>();

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(w.universeCSV + "leagues.csv").openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());

                cw.universeName = w.universeName;
                cw.universeHTML = w.universeHTML;
                cw.universeCSV = w.universeCSV;
                cw.defaultUniverse = w.defaultUniverse;
                cw.valid = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                cw.leagueMap = LeagueList;

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String dataDir = getDataDir(getContext());
                    int count;
                    InputStream input = null;
                    OutputStream output = null;
                    File outFile = null;
                    URLConnection conn = null;
                    URL url = new URL(w.universeCSV + "leagues.csv");
                    conn = url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    input = new BufferedInputStream(url.openStream(), 8192);
                    output = new FileOutputStream(dataDir + "/tmp_leagues.csv");
                    byte data[] = new byte[1024];
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }
                    outFile = new File(dataDir + "/tmp_leagues.csv");

                    CsvReader csvReader = new CsvReader();
                    csvReader.setContainsHeader(true);
                    FileReader reader = new FileReader(outFile);
                    try {
                        CsvParser csvParser = csvReader.parse(reader);
                        CsvRow row;
                        while ((row = csvParser.nextRow()) != null) {
                            LeagueList.put(Integer.parseInt(row.getField("league_id")), row.getField("name"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cw.leagueMap = LeagueList;
                }
            }
            catch (Exception e) {
                e.printStackTrace();;
            }

            return cw;
        }

        @Override
        protected void onPostExecute(CheckWrapper2 cw) {
            ManageUniverses.this.dialog.dismiss();
            final CheckWrapper2 cw2 = cw;
            Map<Integer, String> retVal = cw.leagueMap;
            if (cw.valid) {
                if (retVal.size() < 1) {
                    final Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.no_leagues_in_csv, Snackbar.LENGTH_LONG);
                    final View snackBarView = mySnackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorError));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    mySnackbar.show();
                    mySnackbar.addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            fab.performClick();
                        }
                    });
                } else if (retVal.size() == 1) {
                    Map.Entry<Integer, String> entry = retVal.entrySet().iterator().next();
                    //Integer key = entry.getKey();
                    Universe universe = new Universe();
                    universe.setName(cw2.universeName);
                    universe.setCSV(cw2.universeCSV);
                    universe.setHTML(cw2.universeHTML);
                    universe.setDefaultUniverse(cw2.defaultUniverse);
                    universe.setDefaultLeague(entry.getKey());
                    long foo = db.addUniverseToList(universe);
                    if (foo > -1) {
                        clearListView();
                        populateListView(getContext());
                    } else {
                        final Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.error_adding_universe, Snackbar.LENGTH_LONG);
                        final View snackBarView = mySnackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorError));
                        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        mySnackbar.show();
                        fab.animate().translationYBy(-mySnackbar.getView().getHeight());
                        mySnackbar.addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                fab.animate().translationYBy(mySnackbar.getView().getHeight());
                                fab.performClick();
                            }
                        });
                    }
                } else {
                    final Map<Integer, String> leagueMap = retVal;
                    final Map<String, Integer> displayMap = new HashMap<>();
                    for(Map.Entry<Integer, String> entry : retVal.entrySet()){
                        displayMap.put(entry.getValue(), entry.getKey());
                    }
                    LayoutInflater li = LayoutInflater.from(view.getContext());
                    final View promptsView = li.inflate(R.layout.default_league_prompt, null);
                    final Spinner spinner = promptsView.findViewById(R.id.defaultLeagueSpinner);
                    List<String> leagueList = new ArrayList<String>(displayMap.keySet());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_item, leagueList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(promptsView);
                    builder
                            .setCancelable(true)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            Universe universe = new Universe();
                                            universe.setName(cw2.universeName);
                                            universe.setCSV(cw2.universeCSV);
                                            universe.setHTML(cw2.universeHTML);
                                            universe.setDefaultUniverse(cw2.defaultUniverse);
                                            universe.setDefaultLeague(displayMap.get(spinner.getSelectedItem().toString()));
                                            long foo = db.addUniverseToList(universe);
                                            if (foo > -1) {
                                                android.support.v4.app.Fragment fragment = new ManageUniverses();
                                                String fragmentTag = "nav_switch";
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.currentFrame, fragment, fragmentTag);
                                                ft.commit();
                                                //clearListView();
                                                //populateListView(getContext());
                                            } else {
                                                final Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.error_adding_universe, Snackbar.LENGTH_LONG);
                                                final View snackBarView = mySnackbar.getView();
                                                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorError));
                                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                                textView.setTextColor(Color.WHITE);
                                                mySnackbar.show();
                                                fab.animate().translationYBy(-mySnackbar.getView().getHeight());
                                                mySnackbar.addCallback(new Snackbar.Callback() {
                                                    @Override
                                                    public void onDismissed(Snackbar snackbar, int event) {
                                                        fab.animate().translationYBy(mySnackbar.getView().getHeight());
                                                        fab.performClick();
                                                    }
                                                });
                                            }
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else {
                final Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.invalid_csv_path, Snackbar.LENGTH_LONG);
                final View snackBarView = mySnackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorError));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                mySnackbar.show();
                mySnackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        reShow(cw2.universeName, "", cw2.universeHTML, cw2.defaultUniverse);
                    }
                });
            }

        }
    }

    public static String getDataDir(Context context) throws Exception {
        return context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .applicationInfo.dataDir;
    }

    public void reShow(String name, String csv, String html, Boolean checked) {
        LayoutInflater li = LayoutInflater.from(getContext());
        final View editView = li.inflate(R.layout.universe_edit_prompt, null);

        final TextView universeName = editView.findViewById(R.id.universeName);
        universeName.setText(name);
        ArrayList<InputFilter> curInputFilters = new ArrayList<InputFilter>(Arrays.asList(universeName.getFilters()));
        curInputFilters.add(0, new AlphaNumericInputFilter());
        InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
        universeName.setFilters(newInputFilters);

        final TextView universeCSV = editView.findViewById(R.id.universeCSV);
        universeCSV.setText(csv);

        final TextView universeHTML = editView.findViewById(R.id.universeHTML);
        universeHTML.setText(html);

        final CheckBox defaultUniverse = editView.findViewById(R.id.defaultUniverse);
        defaultUniverse.setChecked(checked);

        final Spinner spinner = editView.findViewById(R.id.defaultLeagueSpinner);
        spinner.setVisibility(View.GONE);

        if (name.length() < 1) {
            universeName.requestFocus();
        } else if (csv.length() < 1) {
            universeCSV.requestFocus();
        } else if (html.length() < 1) {
            universeHTML.requestFocus();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(editView);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        EditText name = editView.findViewById(R.id.universeName);
                        EditText csv = editView.findViewById(R.id.universeCSV);
                        EditText html = editView.findViewById(R.id.universeHTML);
                        CheckBox defaultUniverseCB = editView.findViewById(R.id.defaultUniverse);
                        String universeCSV = csv.getText().toString();
                        if (!universeCSV.endsWith("/")) {
                            universeCSV += "/";
                        }
                        String universeName = name.getText().toString();
                        boolean defaultUniverse = defaultUniverseCB.isChecked();
                        UniverseWrapper w = new UniverseWrapper();
                        w.universeName = universeName;
                        w.universeCSV = universeCSV;
                        w.universeHTML = "";
                        w.defaultUniverse = defaultUniverse;

                        new createUniverse().execute(w);
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
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private class verifyURL extends AsyncTask<String, Void, HashMap<String, Boolean>> {

        HashMap<String, Boolean> validationMap = new HashMap<String, Boolean>();
        Boolean proceed = true;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected HashMap<String, Boolean> doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                validationMap.put("csv", con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                validationMap.put("csv", false);
            }

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(params[1]).openConnection();
                con.setRequestMethod("HEAD");
                validationMap.put("html", con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                e.printStackTrace();
                validationMap.put("html", false);
            }

            return validationMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, Boolean> result) {
            System.out.println(result);

            if (name == null || name.length() == 0) {
                name.setText("");
                name.setHint("Universe name cannot be blank");
                proceed = false;
            } else if (existingUniverses.contains(universeName)) {
                name.setText("");
                name.setHint("Universe name already exists");
                proceed = false;
            }
            if (result.get("csv") != true) {
                csv.setText("");
                csv.setHint(R.string.invalid_csv_path);
                proceed = false;
            }
            if (result.get("html") != true) {
                if (proceed == true && html.getText().toString().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    System.out.println("Proceeding");
                                    UniverseWrapper w = new UniverseWrapper();
                                    w.universeName = name.getText().toString();
                                    w.universeCSV = csv.getText().toString();
                                    w.universeHTML = html.getText().toString();
                                    w.defaultUniverse = defaultUniverseCB.isChecked();
                                    new createUniverse().execute(w);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    System.out.println("Not doing anything");
                                    proceed = false;
                                    break;
                            }
                        }
                    };

                    builder.setMessage("Are you sure you wish to proceed without a valid HTML path?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                } else {
                    html.setText("");
                    html.setHint(R.string.invalid_html_path);
                    proceed = false;
                }
            } else {
                UniverseWrapper w = new UniverseWrapper();
                w.universeName = name.getText().toString();
                w.universeCSV = csv.getText().toString();
                w.universeHTML = html.getText().toString();
                w.defaultUniverse = defaultUniverseCB.isChecked();
                new createUniverse().execute(w);
            }

        }
    }

    protected boolean validate() {
        if (validator != null)
            validator.validate();
        return validated;           // would be set in one of the callbacks below
    }

    @Override
    public void onValidationSucceeded() {
        validated = true;
        UniverseWrapper w = new UniverseWrapper();
        w.universeName = name.getText().toString();
        w.universeCSV = csv.getText().toString();
        w.universeHTML = html.getText().toString();
        w.defaultUniverse = defaultUniverseCB.isChecked();
        new createUniverse().execute(w);
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
            String universeName = editText.getText().toString().trim();
            return !existingUniverses.contains(universeName);
        }

        @Override
        public String getMessage(Context context) {
            return "Universe name already exists";
        }
    }

}
