import java.io.IOException;

/*
 * JNI part ---> Problem Desc: JNI do not run sudo into eclipse!
 */
public class NativeSpoofing {
	static{
		System.loadLibrary("NativeSpoofing");
	}
	public native void sender(String str);
}
