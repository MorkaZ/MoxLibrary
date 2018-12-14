package com.morkaz.moxlibrary.data.vectors;

public class Vector3f {

	private Float f1 = 0f, f2 = 0f, f3 = 0f;

	public Vector3f(Float f1, Float f2, Float f3) {
		if (f1 != null){
			this.f1 = f1;
		}
		if (f2 != null){
			this.f2 = f2;
		}
		if (f3 != null){
			this.f3 = f3;
		}
	}

	public Float getF1() {
		return f1;
	}

	public void setF1(Float f1) {
		this.f1 = f1;
	}

	public Float getF2() {
		return f2;
	}

	public void setF2(Float f2) {
		this.f2 = f2;
	}

	public Float getF3() {
		return f3;
	}

	public void setF3(Float f3) {
		this.f3 = f3;
	}
}
