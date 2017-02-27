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
        return "xl";
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
        xl_index('\ue60b'),
        xl_coffee('\ue60c'),
        xl_gwc('\ue60d'),
        xl_mine('\ue60a'),
        xl_favorite('\ue600'),
        xl_payment('\ue60e'),
        xl_indent('\ue602'),
        xl_shipments('\ue603'),
        xl_site('\ue60a'),
        xl_address('\ue604'),
        xl_my('\ue605'),
        xl_receive('\ue606'),
        xl_set('\ue607'),
        xl_inform('\ue608'),
        xl_estimate('\ue611'),
        xl_enter('\ue601'),
        xl_back('\ue60f'),
        xl_personage('\ue614'),
        xl_update('\ue613'),
        xl_set_my('\ue612'),
        xl_order_inform('\ue615'),
        xl_set_inform('\ue609'),
        xl_inform_deal('\ue616'),
        xl_inform_interaction('\ue617'),
        xl_login_id('\ue619'),
        xl_login_password('\ue618'),
        xl_shoping_close('\ue61a'),
        xl_order_iv('\ue61b'),
        xl_weixin('\ue61d'),
        xl_zfb('\ue61e'),
        xl_yue('\ue61c'),
        xl_favorite_2('\ue61f'),
        xl_logistics('\ue620'),
        xl_share('\ue648'),
        xl_comment('\ue655'),
        xl_portrait('\ue64f'),
        xl_new('\ue638'),
        xl_network_error('\ue677'),
        xl_cart_none('\ue663'),
        xl_made_none('\ue622'),
        xl_location_none('\ue693'),
        xl_logistics_none('\ue623'),
        xl_head_collect('\ue64c'),
        xl_head_share('\ue62b'),
        xl_head_config('\ue624');

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
