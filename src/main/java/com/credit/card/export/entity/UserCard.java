package com.credit.card.export.entity;

import java.util.List;

public class UserCard {
    private  String userName;
    private List<String> userCards;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getUserCards() {
        return userCards;
    }

    public void setUserCards(List<String> userCards) {
        this.userCards = userCards;
    }
}
