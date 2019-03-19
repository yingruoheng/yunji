package util;

public  enum  ValidationPatternEnum {
    USERID("^[1-9][0-9]{7}$"),
    POSITIVE_INTEGER("^[1-9][0-9]*$"),
    ARTICLEID("^([1-9][0-9]{9})|(0)$");




    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    private ValidationPatternEnum(String pattern) {
        this.pattern = pattern;
    }
}
