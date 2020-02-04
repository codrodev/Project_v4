package dm.sime.com.kharetati.utility;

import java.util.ArrayList;

public class CaseInsensitiveArrayListString extends ArrayList<String> {

        @Override
        public boolean contains(Object o) {
            String paramStr = (String)o;
            for (String s : this) {
                if (paramStr.equalsIgnoreCase(s)) return true;
            }
            return false;
        }

}
