package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.VenueFieldPrice;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/3/7.
 */
public class GetVenueFieldPriceResBody implements Serializable {
	public ArrayList<VenueFieldPrice> venueFieldPrice = new ArrayList<VenueFieldPrice>();
}
