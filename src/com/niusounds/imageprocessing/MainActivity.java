package com.niusounds.imageprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.ImageView;
import android.widget.Toast;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu)
public class MainActivity extends Activity {
	@ViewById
	ImageView image;

	Bitmap original;
	Bitmap bmp;
	RenderScript rs;
	Allocation in;
	Allocation out;
	ScriptC_filter1 filter1;
	ScriptC_filter2 filter2;
	ScriptC_filter3 filter3;
	ScriptC_filter4 filter4;
	ScriptC_filter5 filter5;
	ScriptC_filter6 filter6;
	ScriptC_filter7 filter7;
	ScriptC_filter8 filter8;

	@AfterInject
	@Background
	void init() {
		original = BitmapFactory.decodeResource(getResources(), R.drawable.test);

		rs = RenderScript.create(this);
		filter1 = new ScriptC_filter1(rs);
		filter2 = new ScriptC_filter2(rs);
		filter3 = new ScriptC_filter3(rs);
		filter4 = new ScriptC_filter4(rs);
		filter5 = new ScriptC_filter5(rs);
		filter6 = new ScriptC_filter6(rs);
		filter7 = new ScriptC_filter7(rs);
		filter8 = new ScriptC_filter8(rs);
	}

	@Click
	void btn1() {
		// RenderScript準備
		prepareAllocation();

		// 画像処理
		filter1.bind_pixels(in);
		filter1.set_width(bmp.getWidth());
		filter1.set_height(bmp.getHeight());
		filter1.forEach_root(in, out);

		// 後処理と表示
		applyBitmap();
	}

	@Click
	void btn2() {
		prepareAllocation();
		filter2.bind_pixels(in);
		filter2.set_width(bmp.getWidth());
		filter2.set_height(bmp.getHeight());
		filter2.forEach_root(in, out);
		applyBitmap();
	}

	@Click
	void btn3() {
		prepareAllocation();
		filter3.bind_pixels(in);
		filter3.set_width(bmp.getWidth());
		filter3.set_height(bmp.getHeight());
		filter3.forEach_root(in, out);
		applyBitmap();
	}

	@Click
	void btn4() {
		prepareAllocation();
		filter4.bind_pixels(in);
		filter4.set_width(bmp.getWidth());
		filter4.set_height(bmp.getHeight());
		filter4.forEach_root(in, out);
		applyBitmap();
	}

	@Click
	void btn5() {
		prepareAllocation();
		filter5.bind_pixels(in);
		filter5.set_width(bmp.getWidth());
		filter5.set_height(bmp.getHeight());
		filter5.forEach_root(in, out);
		applyBitmap();
	}

	@Click
	void btn6() {
		prepareAllocation();
		filter6.bind_pixels(in);
		filter6.set_width(bmp.getWidth());
		filter6.set_height(bmp.getHeight());
		filter6.forEach_root(in, out);
		applyBitmap();
	}

	@Click
	void btn7() {
		prepareAllocation();
		filter7.bind_pixels(in);
		filter7.set_width(bmp.getWidth());
		filter7.set_height(bmp.getHeight());
		filter7.forEach_root(in, out);
		applyBitmap();
	}

	@Click
	void btn8() {
		prepareAllocation();
		filter8.bind_pixels(in);
		filter8.set_width(bmp.getWidth());
		filter8.set_height(bmp.getHeight());
		filter8.forEach_root(in, out);
		applyBitmap();
	}

	private void prepareAllocation() {
		bmp = original.copy(Config.ARGB_8888, true);
		in = Allocation.createFromBitmap(rs, bmp, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		out = Allocation.createTyped(rs, in.getType());
	}

	private void applyBitmap() {
		out.copyTo(bmp);
		image.setImageBitmap(bmp);

		// 後片付け
		in.destroy();
		out.destroy();
		in = null;
		out = null;
	}

	@OptionsItem
	void menuRevert() {
		// 元の画像を読み込み
		image.setImageBitmap(original);
	}

	@OptionsItem
	void menuLoad() {
		startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), 1);
	}

	Uri tmpUri;
	File tmpFile;

	@OnActivityResult(1)
	void loadResult(int result, Intent data) {
		if (result == RESULT_OK) {
			Uri uri = data.getData();
			try {
				tmpFile = File.createTempFile("tmp", null, getExternalCacheDir());
				tmpUri = Uri.fromFile(tmpFile);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			startActivityForResult(new Intent("com.android.camera.action.CROP")
					.setDataAndType(uri, "image/*")
					.putExtra("crop", "true")
					.putExtra("aspectX", 1)
					.putExtra("aspectY", 1)
					.putExtra(MediaStore.EXTRA_OUTPUT, tmpUri)
					.putExtra("noFaceDetection", true)
					.putExtra("return-data", false)
					, 2);
		}
	}

	@OnActivityResult(2)
	void loadImage(int result, Intent data) {
		if (result == RESULT_OK && tmpFile != null) {
			Bitmap bmp = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
			original = Bitmap.createScaledBitmap(bmp, 512, 512, true);
			image.setImageBitmap(original);
		}
	}

	@OptionsItem
	@Background
	void menuSave() {
		Bitmap bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
		dir.mkdirs();
		File file = new File(dir, System.currentTimeMillis() + ".webp");

		try {
			bmp.compress(CompressFormat.WEBP, 100, new FileOutputStream(file));
			MediaScannerConnection.scanFile(this, new String[] { file.getAbsolutePath() }, new String[] { "image/webp" }, null);
			saveEnd();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@UiThread
	void saveEnd() {
		Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();
	}
}
