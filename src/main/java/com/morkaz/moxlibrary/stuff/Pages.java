package com.morkaz.moxlibrary.stuff;

import javax.annotation.Nullable;
import java.util.*;

public class Pages<T> {

	private Collection<T> objectCollection;
	private Integer objectsPerPage = 20;
	private Map<Integer, Collection<T>> pagesMap = new LinkedHashMap<>();


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
		return pagesMap.get(pageNumber);
	}

	public Integer getLastPageNumber(){
		return pagesMap.size();
	}

	private void mapPages(){
		pagesMap.clear();
		Integer counter = 0;
		Integer actualPage = 1;
		List<T> pageObjects = new ArrayList<>();
		for (T object : objectCollection){
			counter++;
			if (counter <= objectsPerPage){
				pageObjects.add(object);
			} else {
				pagesMap.put(actualPage, new ArrayList(pageObjects));
				pageObjects.clear();
				counter = 0;
				actualPage++;
				pageObjects.add(object);
			}
		}
		if (pageObjects.size() > 0){
			pagesMap.put(actualPage, new ArrayList(pageObjects));
		}
	}



}
