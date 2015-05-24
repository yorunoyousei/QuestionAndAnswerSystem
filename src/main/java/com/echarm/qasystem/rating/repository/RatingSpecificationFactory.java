package com.echarm.qasystem.rating.repository;

import com.echarm.qasystem.rating.model.Rating;

/** 
 * @author yorunosora
 * @since 2015-03-28
 *
 */
public class RatingSpecificationFactory {
	
	public static RatingSpecification getFindRatingAllSpecification(Rating rating) {
		return new FindRatingAllSpecification(rating);
	}
	
	public static RatingSpecification getFindRatingByFilterSpecification(Rating rating){
		//TODO
		return new FindRatingByFilterSpecification(rating);
	}
	
	public static RatingSpecification getFindRatingByIdSpecification(
			Rating rating) {
		return new FindRatingByIdSpecification(rating);
	}
	
	public static RatingSpecification getFindRatingBySearchSpecification(Rating rating){
		//TODO
		return null;
	} 

	public static RatingSpecification getDeleteRatingAllSpecification(
			Rating rating) {
		return new DeleteRatingAllSpecification(rating);
	}

	

}
