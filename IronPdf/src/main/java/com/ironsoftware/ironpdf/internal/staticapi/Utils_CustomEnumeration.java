package com.ironsoftware.ironpdf.internal.staticapi;


public abstract class Utils_CustomEnumeration implements Comparable<Utils_CustomEnumeration> {

    private String name;
    private int id;

    protected Utils_CustomEnumeration(int id, String name) {
        setId(id);
        setName(name);
    }

    @Override
    public boolean equals(Object obj) {
        boolean tempVar = obj instanceof Utils_CustomEnumeration;
        Utils_CustomEnumeration otherValue = tempVar ? (Utils_CustomEnumeration) obj : null;
        if (tempVar) {
            boolean typeMatches = this.getClass().equals(obj.getClass());
            boolean valueMatches = (Integer.valueOf(getId())).equals(otherValue.getId());

            return typeMatches && valueMatches;
        } else {
            return false;
        }
    }

    protected final int getId() {
        return id;
    }

    private void setId(int value) {
        id = value;
    }

    @Override
    public String toString() {
        return getName();
    }

    protected final String getName() {
        return name;
    }

    private void setName(String value) {
        name = value;
    }

    public final int compareTo(Utils_CustomEnumeration o) {
        return Integer.compare(getId(), o.getId());
    }

}
