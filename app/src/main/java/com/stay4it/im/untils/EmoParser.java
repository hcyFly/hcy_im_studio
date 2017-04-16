package com.stay4it.im.untils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stay
 * @version create time：Oct 17, 2014 12:07:10 PM
 */
public class EmoParser {
	public static SpannableStringBuilder parseEmo(Context context,String content) {
		// [smiley_00]sss[smiley_00]xx[smiley_00][smiley_00]
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Pattern pattern = Pattern.compile("\\[smiley_(.*?)\\]");
		Matcher matcher = pattern.matcher(content);
		String emo = null;
		int id = 0;
		float density = context.getResources().getDisplayMetrics().density;
		int emoSize = (int)(density * 25);
		Drawable drawable = null;
		while (matcher.find()) {
			emo = matcher.group();
			emo = emo.substring(1, emo.length() - 1);
			id = context.getResources().getIdentifier(emo, "drawable", context.getPackageName());
			if(id != 0){
				drawable = context.getResources().getDrawable(id);
				drawable.setBounds(0, 0, emoSize, emoSize);
				ImageSpan span = new ImageSpan(drawable);
				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return builder;

	}
}
