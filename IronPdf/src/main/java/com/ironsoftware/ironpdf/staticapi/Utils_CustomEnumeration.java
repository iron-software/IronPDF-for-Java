package com.ironsoftware.ironpdf.staticapi;


public abstract class Utils_CustomEnumeration implements Comparable<Utils_CustomEnumeration> {

    private String Name;
    private int Id;

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
        return Id;
    }

    private void setId(int value) {
        Id = value;
    }

    @Override
    public String toString() {
        return getName();
    }

    protected final String getName() {
        return Name;
    }

    private void setName(String value) {
        Name = value;
    }

    public final int compareTo(Utils_CustomEnumeration o) {
        return Integer.compare(getId(), o.getId());
    }

}
