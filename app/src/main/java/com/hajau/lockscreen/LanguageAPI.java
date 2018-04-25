package com.hajau.lockscreen;

import android.annotation.SuppressLint;

public class LanguageAPI {
	private char dauThanh = '_';
	private String KHONG_DAU = "aăâeêioôơuưyaăâeêioôơuưyaăâeêioôơuưyaăâeêioôơuưyaăâeêioôơuưy";
	private String CO_DAU = "àằầèềìòồờùừỳáắấéếíóốớúứýảẳẩẻểỉỏổởủửỷãẵẫẽễĩõỗỡũữỹạặậẹệịọộợụựỵ";
	private String[] PAD_HAN = { "ㅂ", "ㄱ", "ㄹ", "ㄸ", "ㄲ", "ㅎ", "ㄴ", "ㅁ", "ㅅ", "ㄷ", "凵", "ㅋ", "ㅇ", "ヒ", "ㅍ", "ㅌ", "ㅈ",
			"ㅊ", "ò" };
	private String[] PAD_VIET = { "bb", "kk", "zz", "dd", "gg", "hh", "nn", "mm", "ss", "tt", "vv", "kh", "ng", "nh",
			"ph", "th", "ch", "tr", "wo" };
	private String[] AVN_HAN = { "〒", "工", "ㅡ", "ㅠ", "ㅜ", "山", "ㅗ", "ㅛ" };
	private String[] AVN_VIET = { "uu", "uo", "uw", "uy", "ux", "oo", "of", "ox" };
	private String[] AVD_HAN = { "ㅑ", "ㅕ", "ㅒ", "ㅔ", "ㅐ", "下", "ㅖ", "干", "ㅓ", "ㅏ", "ㅣ" };
	private String[] AVD_VIET = { "aw", "aa", "ie", "ee", "ex", "ai", "ia", "oi", "ow", "ax", "ii" };
	private String[] PAC_1_KI_TU_HAN = { "ㄹ", "ㅅ", "ㅋ", "ㅍ", "ㅌ", "ㅈ", "ㅇ", "ヒ", "ㅂ", "ㄱ", "ㄷ", "ㄴ", "ㅁ", "二" };
	private String[] PAC_1_KI_TU_VIET = { "zz", "ss", "kh", "ph", "th", "cc", "ng", "nh", "pc", "kc", "tc", "lw", "mw",
			"yw" };
	private String[] PAC_2_KI_TU_HAN1 = { "ㅂ", "ㄴ", "ㅁ", "ㄷ", "ㅇ", "ヒ", "ㅈ", "ㄱ", "二" };
	private String[] PAC_2_KI_TU_V1 = { "p", "l", "m", "t", "g", "h", "c", "k", "y" };
	private String[] PAC_2_KI_TU_HAN2 = { "ㄴ", "ㄱ", "ㅅ", "ㅁ", "ㅇ", };
	private String[] PAC_2_KI_TU_V2 = { "l", "c", "s", "m", "g" };
	private final String BLANK = "";
	private String string = BLANK;

	public LanguageAPI(String s) {
		this.string = s;
	}

	public LanguageAPI setText(String string) {
		this.string = string;
		return this;
	}

	@Override
	public String toString() {
		String data = conver(string);
		String PAD = data.substring(0, 2);
		String AVN = data.substring(2, 4);
		String AVD = data.substring(4, 6);
		String PAC = data.substring(6);
		String result = PAD;
		if (!AVN.equals("wo"))
			result += AVN;
		if (!AVD.equals("wo"))
			result += AVD;
		if (!PAC.equals("wo"))
			result += PAC;
		return result;
	}

	private String vi2han(String[] vi, String[] han, String am) {
		for (int i = 0; i < vi.length; i++) {
			if (am.equals(vi[i]))
				return han[i];
		}
		return BLANK;
	}

	public String toHangul() {
		String data = conver(string);
		String PAD = data.substring(0, 2);
		String AVN = data.substring(2, 4);
		String AVD = data.substring(4, 6);
		String PAC = data.substring(6);
		String result = vi2han(PAD_VIET, PAD_HAN, PAD);
		result += vi2han(AVN_VIET, AVN_HAN, AVN);
		result += vi2han(AVD_VIET, AVD_HAN, AVD);
		String PACHAN = vi2han(PAC_1_KI_TU_VIET, PAC_1_KI_TU_HAN, PAC);
		if (PACHAN.equals(BLANK)) {
			result += vi2han(PAC_2_KI_TU_V1, PAC_2_KI_TU_HAN1, PAC.substring(0, 1))
					+ vi2han(PAC_2_KI_TU_V2, PAC_2_KI_TU_HAN2, PAC.substring(1));
		} else
			result += PACHAN;
		return result;
	}

	private String chuyenKhongDau(String str1) {
		int j;
		dauThanh = '_';
		for (int i = 0; i < str1.length(); i++)
			if ((j = CO_DAU.indexOf(str1.charAt(i))) != -1) {

				if (j < 12)
					dauThanh = 'l';
				else if (j < 24)
					dauThanh = 'c';
				else if (j < 36)
					dauThanh = 's';
				else if (j < 48)
					dauThanh = 'm';
				else
					dauThanh = 'g';
				return str1.replace(str1.charAt(i), KHONG_DAU.charAt(j));
			}
		return str1;
	}

	private boolean laPhuAmDau(char c) {
		if ("jzđqrtpsdghklxcvbnm".indexOf(c) != -1)
			return true;
		return false;
	}

	private boolean laPhuAmCuoi(char c) {
		if ("pbdrgđhmnlckqsxvty".indexOf(c) != -1)
			return true;
		return false;
	}

	private boolean laNguyenAm(char c) {
		if ("aăâeêioôơuư".indexOf(c) != -1)
			return true;
		return false;
	}

	@SuppressLint("DefaultLocale")
	private String conver(String z) {

		// ***************************************************************
		// * *
		// *______________BẮT ĐẦU XỬ LÝ PhẦN PHỤ ÂM ĐẦU__________________*
		// * *
		// * *
		// ***************************************************************
		String code = BLANK;
		String PAD = "wo", AVD = "wo", AVN = "wo", PAC = "wo";
		z = z.toLowerCase();
		z = chuyenKhongDau(z);
		if (z.length() == 1 && !z.equals("y") && !laNguyenAm(z.charAt(0)))
			return z;
		z = z.replace("ngh", "ng");
		if (z.charAt(0) == 'q') {
			z = z.replace("qua", "koa");
			z = z.replace("que", "koe");
			z = z.replace("quă", "koă");
		}
		int j;
		if ((j = z.indexOf('y')) != -1)
			if ((!laNguyenAm(z.charAt(j - 1)) || j != z.length() - 1) && z.charAt(j + 1) != 'a'
					&& z.charAt(j + 1) != 'n')
				z = z.replace('y', 'i');
		if (z.substring(0, 2).equals("gi"))
			if (laNguyenAm(z.charAt(2)))
				z = z.replace("gi", "r");
			else
				z = z.replace('g', 'r');
		if (laPhuAmDau(z.charAt(0))) {
			if (laPhuAmDau(z.charAt(1))) {
				PAD = z.substring(0, 2);
				if (PAD.equals("gh"))
					PAD = "gg";
			} else {
				if (z.charAt(0) == 'l')
					PAD = "nn";
				else if (z.charAt(0) == 'x')
					PAD = "ss";
				else if (z.charAt(0) == 'p')
					PAD = "bb";
				else if (z.charAt(0) == 'đ')
					PAD = "dd";
				else if (z.charAt(0) == 'c' || z.charAt(0) == 'q')
					PAD = "kk";
				else if (z.charAt(0) == 'r' || z.charAt(0) == 'd')
					PAD = "zz";
				else
					PAD = z.charAt(0) + BLANK + z.charAt(0);
			}
		}

		String[] BANG_VAN = { "iêu", "oai", "uôi", "ươi", "ươu", "uya", "uiê", "ai", "ao", "au", "âu", "eo", "êu", "ia",
				"iê", "iu", "oa", "oă", "oe", "oi", "ôi", "ơi", "oo", "ua", "uâ", "ưa", "uê", "ui", "ưi", "uô", "uơ",
				"ươ", "ưu", "uy", "a", "ă", "â", "e", "ê", "i", "o", "ô", "ơ", "u", "ư" };
		String[] BANG_MA_HOA = { "uxie", "ofai", "uoii", "uwoi", "uuow", "uyax", "uxie", "woai", "oxax", "uxaw", "uxaa",
				"oxex", "uxex", "woia", "woie", "uxow", "ofax", "ofaw", "ofex", "oxii", "ofii", "wooi", "oowo", "uxax",
				"uxaa", "uwax", "uxee", "uxii", "uwii", "uowo", "uwow", "uwow", "uuwo", "uywo", "woax", "woaw", "woaa",
				"woex", "woee", "woii", "oxwo", "ofwo", "woow", "uxwo", "uwwo" };
		for (j = 0; j < BANG_VAN.length; j++) {
			if (z.indexOf(BANG_VAN[j]) != -1) {
				AVN = BANG_MA_HOA[j].substring(0, 2);
				AVD = BANG_MA_HOA[j].substring(2);
				break;
			}
		}

		if (laPhuAmCuoi(z.charAt(z.length() - 1)) && !z.substring(z.length() - 2).equals("uy")) {

			// Phu am cuoi 2 ki tu
			if (laPhuAmCuoi(z.charAt(z.length() - 2))) {
				PAC = z.substring(z.length() - 2);
				if (dauThanh != '_') {
					if (PAC.equals("ng"))
						PAC = "g" + dauThanh;
					if (PAC.equals("nh"))
						PAC = "h" + dauThanh;
					if (PAC.equals("ch"))
						PAC = "c" + dauThanh;
				}
			}
			// Phu am cuoi 1 ki tu
			else if (!z.substring(z.length() - 2).equals("uy")) {
				PAC = z.substring(z.length() - 1);
				if (PAC.equals("n"))
					PAC = "l";
				if (PAC.equals("c"))
					PAC = "k";
				if (dauThanh == '_')
					PAC = PAC + 'w';
				else
					PAC = PAC + dauThanh;
			}
		} else
		// Khong phu am cuoi
		{
			if (dauThanh == '_')
				PAC = "wo";
			else if (dauThanh == 'l')
				PAC = "th";
			else if (dauThanh == 's')
				PAC = "ss";
			else if (dauThanh == 'm')
				PAC = "zz";
			else if (dauThanh == 'c')
				PAC = "kh";
			else
				PAC = "ph";
		}
		code = PAD + AVN + AVD + PAC;
		return code;
	}
}