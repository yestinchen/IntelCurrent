package com.kernel.intelcurrent.widget;

import java.lang.ref.WeakReference;
import android.os.AsyncTask;
/**为了避免强引用造成的OOM问题，使用若引用解决
 * @author sheling*/
public abstract class WeakAsyncTask<Params,Progress,Result,WeakTarget> extends
	AsyncTask<Params,Progress,Result> {

	protected WeakReference<WeakTarget> mTarget;
	
	public WeakAsyncTask(WeakTarget target){
		mTarget = new WeakReference<WeakTarget>(target);
	}
	
	@Override
	protected void onPostExecute(Result result) {
		final WeakTarget target = mTarget.get();
		if(target != null)	this.onPostExecute(target,result);
	}

	private void onPostExecute(WeakTarget target, Result result) {};

	@Override
	protected void onPreExecute() {
		final WeakTarget target = mTarget.get();
		if(target != null)	this.onPreExecute(target);
	}

	private void onPreExecute(WeakTarget target) {};

	@SuppressWarnings("unchecked")
	@Override
	protected Object doInBackground(Object... params) {
		final WeakTarget target = mTarget.get();
		if(target != null)	return this.doInBackground(target,params);
		else return null;
	}

	protected void doInbackground(WeakTarget target,Object... params){};
}
