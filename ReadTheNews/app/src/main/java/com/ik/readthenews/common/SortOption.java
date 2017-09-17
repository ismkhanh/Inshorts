package com.ik.readthenews.common;


public enum SortOption {
    PUBLISHER, CATEGORY, OLD_TO_NEW, NEW_TO_OLD;

    public static SortOption toSortOption (String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception ex) {
            // For error cases
            return null;
        }
    }
}
