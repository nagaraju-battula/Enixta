/**
 * 
 */
package com.enixta.platform.buysmaart.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author nbattula
 *
 */
public class SmartPhoneRowMapper implements RowMapper{

	SmartPhone smartPhone = null;
	List<SmartPhone> smartPhoneList = new ArrayList<SmartPhone>();
	
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		do {
			SmartPhone smartPhone = new SmartPhone();
			smartPhone.setCompany(rs.getString("F_COMPANY"));
			smartPhone.setDisplayName(rs.getString("F_DISPLAYNAME"));
			smartPhone.setEnabled("YES".equals(rs.getString("F_ISENABLED")) ? true : false);
			smartPhone.setId(rs.getInt("F_DEVICE_ID"));
			smartPhone.setName(rs.getString("F_NAME"));
			smartPhone.setPrice(rs.getLong("F_PRICE"));
			
			smartPhoneList.add(smartPhone);
			
		}while (rs.next());
		return smartPhoneList;
	}

}
