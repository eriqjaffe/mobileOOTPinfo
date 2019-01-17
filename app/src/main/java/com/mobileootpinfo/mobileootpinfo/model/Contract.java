package com.mobileootpinfo.mobileootpinfo.model;

/**
 * Created by eriqj on 3/26/2018.
 */

public class Contract {

    int playerID, teamID, leagueID, currentSalary, currentContractYear, contractLength, totalValue, signedIn,
        salary0, salary1, salary2, salary3, salary4, salary5, salary6, salary7, salary8, salary9,
        lastYearOptionBuyout, nextLastYearOptionBuyout, minimumPABonus, minimumIPBonus, mvpBonus, poyBonus, asBonus,
        noTrade, lastYearTeamOption, lastYearPlayerOption, lastYearVestingOption, nextLastYearTeamOption, nextLastYearPlayerOption, nextLastYearVestingOption,
        minimumPA, minimumIP;
    String firstName, lastName, throwingHand, position, mvpAwardName, poyAwardName;
    Boolean minorLeague;

    public Contract() {}

    public Contract(int playerID, int teamID, int leagueID, int currentSalary, int currentContractYear, int contractLength, int totalValue,
                    int signedIn, int salary0, int salary1, int salary2, int salary3, int salary4, int salary5, int salary6, int salary7,
                    int salary8, int salary9, int lastYearOptionBuyout, int nextLastYearOptionBuyout, int minimumPABonus, int minimumIPBonus,
                    int mvpBonus, int poyBonus, int asBonus, String firstName, String lastName, String throwingHand, String position,
                    int noTrade, int lastYearTeamOption, int lastYearPlayerOption, int lastYearVestingOption, int nextLastYearTeamOption,
                    int nextLastYearPlayerOption, int nextLastYearVestingOption, int minimumPA, int minimumIP, String mvpAwardName, String poyAwardName,
                    boolean minorLeague) {
        this.playerID = playerID;
        this.teamID = teamID;
        this.leagueID = leagueID;
        this.currentSalary = currentSalary;
        this.currentContractYear = currentContractYear;
        this.contractLength = contractLength;
        this.totalValue = totalValue;
        this.signedIn = signedIn;
        this.salary0 = salary0;
        this.salary1 = salary1;
        this.salary2 = salary2;
        this.salary3 = salary3;
        this.salary4 = salary4;
        this.salary5 = salary5;
        this.salary6 = salary6;
        this.salary7 = salary7;
        this.salary8 = salary8;
        this.salary9 = salary9;
        this.lastYearOptionBuyout = lastYearOptionBuyout;
        this.nextLastYearOptionBuyout = nextLastYearOptionBuyout;
        this.minimumPABonus = minimumPABonus;
        this.minimumIPBonus = minimumIPBonus;
        this.mvpBonus = mvpBonus;
        this.poyBonus = poyBonus;
        this.asBonus = asBonus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.throwingHand = throwingHand;
        this.position = position;
        this.noTrade = noTrade;
        this.lastYearTeamOption = lastYearTeamOption;
        this.lastYearPlayerOption = lastYearPlayerOption;
        this.lastYearVestingOption = lastYearVestingOption;
        this.nextLastYearTeamOption = nextLastYearTeamOption;
        this.nextLastYearPlayerOption = nextLastYearPlayerOption;
        this.nextLastYearVestingOption = nextLastYearVestingOption;
        this.minimumPA = minimumPA;
        this.minimumIP = minimumIP;
        this.mvpAwardName = mvpAwardName;
        this.poyAwardName = poyAwardName;
        this.minorLeague = minorLeague;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public int getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(int currentSalary) {
        this.currentSalary = currentSalary;
    }

    public int getCurrentContractYear() {
        return currentContractYear;
    }

    public void setCurrentContractYear(int currentContractYear) {
        this.currentContractYear = currentContractYear;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(int contractLength) {
        this.contractLength = contractLength;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(int signedIn) {
        this.signedIn = signedIn;
    }

    public int getSalary0() {
        return salary0;
    }

    public void setSalary0(int salary0) {
        this.salary0 = salary0;
    }

    public int getSalary1() {
        return salary1;
    }

    public void setSalary1(int salary1) {
        this.salary1 = salary1;
    }

    public int getSalary2() {
        return salary2;
    }

    public void setSalary2(int salary2) {
        this.salary2 = salary2;
    }

    public int getSalary3() {
        return salary3;
    }

    public void setSalary3(int salary3) {
        this.salary3 = salary3;
    }

    public int getSalary4() {
        return salary4;
    }

    public void setSalary4(int salary4) {
        this.salary4 = salary4;
    }

    public int getSalary5() {
        return salary5;
    }

    public void setSalary5(int salary5) {
        this.salary5 = salary5;
    }

    public int getSalary6() {
        return salary6;
    }

    public void setSalary6(int salary6) {
        this.salary6 = salary6;
    }

    public int getSalary7() {
        return salary7;
    }

    public void setSalary7(int salary7) {
        this.salary7 = salary7;
    }

    public int getSalary8() {
        return salary8;
    }

    public void setSalary8(int salary8) {
        this.salary8 = salary8;
    }

    public int getSalary9() {
        return salary9;
    }

    public void setSalary9(int salary9) {
        this.salary9 = salary9;
    }

    public int getLastYearOptionBuyout() {
        return lastYearOptionBuyout;
    }

    public void setLastYearOptionBuyout(int lastYearOptionBuyout) {
        this.lastYearOptionBuyout = lastYearOptionBuyout;
    }

    public int getNextLastYearOptionBuyout() {
        return nextLastYearOptionBuyout;
    }

    public void setNextLastYearOptionBuyout(int nextLastYearOptionBuyout) {
        this.nextLastYearOptionBuyout = nextLastYearOptionBuyout;
    }

    public int getMinimumPABonus() {
        return minimumPABonus;
    }

    public void setMinimumPABonus(int minimumPABonus) {
        this.minimumPABonus = minimumPABonus;
    }

    public int getMinimumIPBonus() {
        return minimumIPBonus;
    }

    public void setMinimumIPBonus(int minimumIPBonus) {
        this.minimumIPBonus = minimumIPBonus;
    }

    public int getMvpBonus() {
        return mvpBonus;
    }

    public void setMvpBonus(int mvpBonus) {
        this.mvpBonus = mvpBonus;
    }

    public int getPoyBonus() {
        return poyBonus;
    }

    public void setPoyBonus(int poyBonus) {
        this.poyBonus = poyBonus;
    }

    public int getAsBonus() {
        return asBonus;
    }

    public void setAsBonus(int asBonus) {
        this.asBonus = asBonus;
    }

    public int getNoTrade() {
        return noTrade;
    }

    public void setNoTrade(int noTrade) {
        this.noTrade = noTrade;
    }

    public int getLastYearTeamOption() {
        return lastYearTeamOption;
    }

    public void setLastYearTeamOption(int lastYearTeamOption) {
        this.lastYearTeamOption = lastYearTeamOption;
    }

    public int getLastYearPlayerOption() {
        return lastYearPlayerOption;
    }

    public void setLastYearPlayerOption(int lastYearPlayerOption) {
        this.lastYearPlayerOption = lastYearPlayerOption;
    }

    public int getLastYearVestingOption() {
        return lastYearVestingOption;
    }

    public void setLastYearVestingOption(int lastYearVestingOption) {
        this.lastYearVestingOption = lastYearVestingOption;
    }

    public int getNextLastYearTeamOption() {
        return nextLastYearTeamOption;
    }

    public void setNextLastYearTeamOption(int nextLastYearTeamOption) {
        this.nextLastYearTeamOption = nextLastYearTeamOption;
    }

    public int getNextLastYearPlayerOption() {
        return nextLastYearPlayerOption;
    }

    public void setNextLastYearPlayerOption(int nextLastYearPlayerOption) {
        this.nextLastYearPlayerOption = nextLastYearPlayerOption;
    }

    public int getNextLastYearVestingOption() {
        return nextLastYearVestingOption;
    }

    public void setNextLastYearVestingOption(int nextLastYearVestingOption) {
        this.nextLastYearVestingOption = nextLastYearVestingOption;
    }

    public int getMinimumPA() {
        return minimumPA;
    }

    public void setMinimumPA(int minimumPA) {
        this.minimumPA = minimumPA;
    }

    public int getMinimumIP() {
        return minimumIP;
    }

    public void setMinimumIP(int minimumIP) {
        this.minimumIP = minimumIP;
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

    public String getThrowingHand() {
        return throwingHand;
    }

    public void setThrowingHand(String throwingHand) {
        this.throwingHand = throwingHand;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMvpAwardName() {
        return mvpAwardName;
    }

    public void setMvpAwardName(String mvpAwardName) {
        this.mvpAwardName = mvpAwardName;
    }

    public String getPoyAwardName() {
        return poyAwardName;
    }

    public void setPoyAwardName(String poyAwardName) {
        this.poyAwardName = poyAwardName;
    }

    public Boolean getMinorLeague() {
        return minorLeague;
    }

    public void setMinorLeague(Boolean minorLeague) {
        this.minorLeague = minorLeague;
    }
}
