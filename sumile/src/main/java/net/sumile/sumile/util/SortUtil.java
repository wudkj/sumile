package net.sumile.sumile.util;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SortUtil implements Comparator<ContactsChooserUtil.NameNumberPair> {
    Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

    @Override
    public int compare(ContactsChooserUtil.NameNumberPair o1, ContactsChooserUtil.NameNumberPair o2) {
        if (cmp.compare(o1.getName(), o2.getName()) > 0) {
            return 1;
        } else if (cmp.compare(o1.getName(), o2.getName()) < 0) {
            return -1;
        }
        return 0;
    }
}