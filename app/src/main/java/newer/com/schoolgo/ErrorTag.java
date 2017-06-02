package newer.com.schoolgo;

/**
 * Created by Administrator on 2017/5/13.
 */

public enum ErrorTag {
    ERROR_MOMENT_LIST(2),
    ERROR_REG_EXISTS(3),
    ERROR_REG_SEV(4),
    ERROR_SCHOOL_ERROR(5),
    ERROR_LOGIN_NAME(6);
    public final int values;

    ErrorTag(int x) {
        this.values = x;
    }
}
