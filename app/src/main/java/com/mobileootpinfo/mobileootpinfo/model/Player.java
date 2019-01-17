package com.mobileootpinfo.mobileootpinfo.model;

public class Player {
    int id;
    String firstName;
    String lastName;
    int uniformNumber;
    int position;
    int playerBats;
    int playerThrows;
    int height;
    int weight;
    String birthday;
    String teamName;
    String teamNickname;
    String backgroundColor;
    String textColor;
    String teamLogo;
    String abbr;
    String leagueLogo;
    String birthCity;
    String birthState;
    String birthCountry;
    int birthCountryId;
    String nationality;
    int localPopularity;
    int nationalPopularity;
    int retired;
    int contractType;

    public Player() {}

    public Player(int id, String firstName, String lastName, int uniformNumber, int position, int playerBats, int playerThrows, int height, int weight,
                  String birthday, String teamName, String teamNickname, String backgroundColor, String textColor, String teamLogo, String abbr, String leagueLogo,
                  String birthCity, String birthState, String birthCountry, int birthCountryId, int localPopularity, int nationalPopularity, int retired, int contractType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.uniformNumber = uniformNumber;
        this.position = position;
        this.playerBats = playerBats;
        this.playerThrows = playerThrows;
        this.height = height;
        this.weight = weight;
        this.birthday = birthday;
        this.teamName = teamName;
        this.teamNickname = teamNickname;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.teamLogo = teamLogo;
        this.abbr = abbr;
        this.leagueLogo = leagueLogo;
        this.birthCity = birthCity;
        this.birthState = birthState;
        this.birthCountry = birthCountry;
        this.birthCountryId = birthCountryId;
        this.nationality = nationality;
        this.localPopularity = localPopularity;
        this.nationalPopularity = nationalPopularity;
        this.retired = retired;
        this.contractType = contractType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUniformNumber() {
        return uniformNumber;
    }

    public void setUniformNumber(int uniformNumber) {
        this.uniformNumber = uniformNumber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPlayerBats() {
        return playerBats;
    }

    public void setPlayerBats(int playerBats) {
        this.playerBats = playerBats;
    }

    public int getPlayerThrows() {
        return playerThrows;
    }

    public void setPlayerThrows(int playerThrows) {
        this.playerThrows = playerThrows;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamNickname() {
        return teamNickname;
    }

    public void setTeamNickname(String teamNickname) {
        this.teamNickname = teamNickname;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getLeagueLogo() {
        return leagueLogo;
    }

    public void setLeagueLogo(String leagueLogo) {
        this.leagueLogo = leagueLogo;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getBirthState() {
        return birthState;
    }

    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public int getBirthCountryId() {
        return birthCountryId;
    }

    public void setBirthCountryId(int birthCountryId) {
        this.birthCountryId = birthCountryId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getLocalPopularity() {
        return localPopularity;
    }

    public void setLocalPopularity(int localPopularity) {
        this.localPopularity = localPopularity;
    }

    public int getNationalPopularity() {
        return nationalPopularity;
    }

    public void setNationalPopularity(int nationalPopularity) {
        this.nationalPopularity = nationalPopularity;
    }

    public int getRetired() {
        return retired;
    }

    public void setRetired(int retired) {
        this.retired = retired;
    }

    public int getContractType() {
        return contractType;
    }

    public void setContractType(int contractType) {
        this.contractType = contractType;
    }
}


