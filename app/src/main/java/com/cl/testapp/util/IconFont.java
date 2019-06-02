package com.cl.testapp.util;

import android.content.Context;
import android.graphics.Typeface;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * IconFont
 * Created by Administrator on 2016-09-18.
 */
public class IconFont implements ITypeface {

    private static final String TTF_FILE = "iconfont.ttf";
    private static Typeface typeface = null;
    private static HashMap<String, Character> mChars;

    @Override
    public IIcon getIcon(String key) {
        return Icon.valueOf(key);
    }

    @Override
    public HashMap<String, Character> getCharacters() {
        if (mChars == null) {
            HashMap<String, Character> aChars = new HashMap<>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), v.character);
            }
            mChars = aChars;
        }
        return mChars;
    }

    @Override
    public String getMappingPrefix() {
        return "icon";
    }

    @Override
    public String getFontName() {
        return "IconFont";
    }

    @Override
    public String getVersion() {
        return "1.0.1";
    }

    @Override
    public int getIconCount() {
        return mChars.size();
    }

    @Override
    public Collection<String> getIcons() {
        Collection<String> icons = new LinkedList<String>();
        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }
        return icons;
    }

    @Override
    public String getAuthor() {
        return "AliBaBa";
    }

    @Override
    public String getUrl() {
        return "http://www.iconfont.cn/";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getLicense() {
        return "";
    }

    @Override
    public String getLicenseUrl() {
        return "http://thx.github.io/";
    }

    @Override
    public Typeface getTypeface(Context ctx) {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(ctx.getAssets(), TTF_FILE);
            } catch (Exception e) {
                return null;
            }
        }
        return typeface;
    }

    public enum Icon implements IIcon {
        icon_mine_fill('\ue70f'),
        icon_add('\ue6df'),
        icon_message('\ue70c'),
        icon_brush('\ue6e5'),
        icon_unfold('\ue749'),
        icon_search('\ue741'),
        icon_undo('\ue739'),
        icon_setup('\ue729'),
        icon_setup_fill('\ue728'),
        icon_return('\ue720'),
        icon_people('\ue716'),
        icon_mine('\ue70e'),
        icon_lock('\ue709'),
        icon_like('\ue708'),
        icon_like_fill('\ue707'),
        icon_interactive('\ue705'),
        icon_interactive_fill('\ue704'),
        icon_homepage('\ue703'),
        icon_homepage_fill('\ue702'),
        icon_enter('\ue6f8'),
        icon_eit('\ue6f5'),
        icon_delete('\ue6f3'),
        icon_delete_fill('\ue6f2'),
        icon_collection('\ue6eb'),
        icon_collection_fill('\ue6ea'),
        icon_close('\ue6e9'),
        icon_camera('\ue6e8'),
        icon_camera_fill('\ue6e7'),
        icon_browse('\ue6e4'),
        icon_accessory('\ue6dd');

        char character;

        Icon(char character) {
            this.character = character;
        }

        @Override
        public String getFormattedName() {
            return "{" + name() + "}";
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public char getCharacter() {
            return character;
        }

        private static ITypeface typeface;

        @Override
        public ITypeface getTypeface() {
            if (typeface == null) {
                typeface = new IconFont();
            }
            return typeface;
        }
    }
}
