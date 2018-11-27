package com.morkaz.moxlibrary.stuff;

import javax.annotation.Nullable;
import java.util.*;

public class Pages<T> {

	private Collection<T> objectCollection;
	private Integer objectsPerPage = 20;
	private Map<Integer, Collection<T>> pagesList = new HashMap<>();


	public Pages(Collection<T> objectCollection, Integer objectsPerPage) {
		this.objectCollection = objectCollection;
		this.objectsPerPage = objectsPerPage;
		this.mapPages();
	}

	public Pages(Collection<T> objectCollection) {
		this.objectCollection = objectCollection;
		this.mapPages();
	}


	@Nullable
	public Collection<T> getObjects(Integer pageNumber){
		return pagesList.get(pageNumber);
	}

	public Integer getMaxPageNumber(){
		return pagesList.size();
	}

	private void mapPages(){
		pagesList.clear();
		Integer counter = 0;
		Integer actualPage = 1;
		List<T> pageObjects = new ArrayList<>();
		for (T object : objectCollection){
			counter++;
			if (counter < objectsPerPage){
				pageObjects.add(object);
			} else {
				pagesList.put(actualPage, new ArrayList(pageObjects));
				pageObjects.clear();
				counter = 0;
				actualPage = 1;
			}
		}
	}



}
