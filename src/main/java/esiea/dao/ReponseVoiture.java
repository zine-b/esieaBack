package esiea.dao;

import esiea.metier.Voiture;

public class ReponseVoiture {

	private Voiture[] data;
	private int volume;
	public Voiture[] getData() {
		return data;
	}
	public void setData(Voiture[] data) {
		this.data = data;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public void setData(Voiture v, int index) {
		if(data == null) {
			data = new Voiture[index+1];
		}
		data[index] = v;
	}
}
