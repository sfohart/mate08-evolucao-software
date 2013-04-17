package br.ufba.dcc.disciplinas.mate08.model;

public class ScoredItem<T> implements Comparable<ScoredItem<T>> {

	private final T item;
	private final Double score;
	
	public ScoredItem(T item, Double score) {
		super();
		this.item = item;
		this.score = score;
	}
	
	public T getItem() {
		return item;
	}
	
	public Double getScore() {
		return score;
	}

	@Override
	public int compareTo(ScoredItem<T> o) {
		return getScore().compareTo(o.getScore());
	}
	
	@Override
	public String toString() {		
		return "Rank: " + getScore() + "\t Item: " + getItem().toString();
	}

}
