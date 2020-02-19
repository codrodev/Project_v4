package dm.sime.com.kharetati.utility.Files;

public abstract class DialogConfigs {
    public static final int SINGLE_MODE = 0;
    public static final int MULTI_MODE = 1;
    public static final int FILE_SELECT = 0;
    public static final int DIR_SELECT = 1;
    public static final int FILE_AND_DIR_SELECT = 2;

    /*  PARENT_DIRECTORY*/
    public static final String DIRECTORY_SEPERATOR = "/";
    public static final String STORAGE_DIR = "mnt";

    /*  DEFAULT_DIR is the default mount point of the SDCARD. It is the default
     *  mount point.
     */
    public static final String DEFAULT_DIR = DIRECTORY_SEPERATOR + STORAGE_DIR;
}