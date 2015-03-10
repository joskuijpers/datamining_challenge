package io.github.joskuijpers.datamining_challenge;

public class User {
    private int index, age, profession;
    private boolean male;
    
	public User(int _index, boolean _male, int _age, int _profession) {
        this.index      = _index;
        this.male       = _male;
        this.age        = _age;
        this.profession = _profession;
    }
    
    public int getIndex() {
        return index;
    }
    
    public boolean isMale() {
        return male;
    }
    
    public int getAge() {
        return age;
    }
    
    public int getProfession() {
        return profession;
    }
    
}
