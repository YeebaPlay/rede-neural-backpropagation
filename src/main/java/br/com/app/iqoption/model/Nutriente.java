package br.com.app.iqoption.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Nutriente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private float CT;
	private float HDL;
	private float LDL;
	private float TG;
	private float glicose;
	
	public Nutriente(Integer id, float CT, float HDL, float LDL, float TG, float glicose){
		this.id = id;
		this.CT = CT;
		this.HDL = HDL;
		this.LDL = LDL;
		this.TG = TG;
		this.glicose = glicose;
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getCT() {
		return CT;
	}
	public void setCT(float cT) {
		CT = cT;
	}
	public float getHDL() {
		return HDL;
	}
	public void setHDL(float hDL) {
		HDL = hDL;
	}
	public float getLDL() {
		return LDL;
	}
	public void setLDL(float lDL) {
		LDL = lDL;
	}
	public float getTG() {
		return TG;
	}
	public void setTG(float tG) {
		TG = tG;
	}
	public float getGlicose() {
		return glicose;
	}
	public void setGlicose(float glicose) {
		this.glicose = glicose;
	}
	
}
