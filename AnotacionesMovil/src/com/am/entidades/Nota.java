package com.am.entidades;

public class Nota {
	private long id;
	private String nota;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return nota;
	}

}
