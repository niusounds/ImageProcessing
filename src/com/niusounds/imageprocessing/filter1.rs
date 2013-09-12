#pragma version(1)
#pragma rs java_package_name(com.niusounds.imageprocessing)

const uchar4 *pixels; // 元画像のピクセルデータ
int width; // 元画像の幅
int height; // 元画像の高さ

void root(const uchar4 *in, uchar4 *out, uint32_t intX, uint32_t intY) {
	// x,y座標を -1.0 - 1.0 に正規化
	float x = ((float) intX / (float) width) * 2.0f - 1.0f;
	float y = ((float) intY / (float) height) * 2.0f - 1.0f;

	// 座標変換
	x = sinpi(x / 2);

	// 正規化座標を整数に戻す
	intX = (x + 1.0f) / 2.0f * width;
	intY = (y + 1.0f) / 2.0f * height;

	// 変換先の座標のピクセルを代入
	*out = pixels[intX + intY * height];
}