package com.oracle.oep.viewusage.processor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ViewUsageRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		ViewUsage usage = new ViewUsage();
		usage.setNetworkSessionid(rs.getString(1).replaceAll("_[0-9]{1,}$", ""));
		usage.setUsageType(rs.getObject(2) == null ? "" :  rs.getObject(2).toString());
		usage.setUnitName(rs.getObject(3) == null ? "" :  rs.getObject(3).toString());
		usage.setStartT(rs.getLong(4));
		usage.setEndT(rs.getLong(5));
		usage.setOriginSid(rs.getObject(6) == null ? "" :  rs.getObject(6).toString());
		usage.setLocAreaCode(rs.getObject(7) == null ? "" :  rs.getObject(7).toString());
		usage.setTerminateCause(rs.getObject(8) == null ? "" :  rs.getObject(8).toString());
		usage.setCalledTo(rs.getObject(9) == null ? "" :  rs.getObject(9).toString());
		usage.setQuantity(rs.getLong(10));
		usage.setImpactCategory(rs.getObject(11) == null ? "" :  rs.getObject(11).toString());
		usage.setAmount(BigDecimal.ZERO);
		usage.setEventNo(rs.getObject(12) == null ? "" :  rs.getObject(12).toString());
		usage.setDescr(rs.getObject(13) == null ? "" :  rs.getObject(13).toString()); // 14 is left behind as this is quantity again
		usage.setEbiResourceId(rs.getObject(15) == null ? "" :  rs.getObject(15).toString());
		usage.setEbiQty(rs.getLong(16));
		usage.setEbiAmt(rs.getBigDecimal(17));
		usage.setOfferName(rs.getObject(18) == null ? "" :  rs.getObject(18).toString());
		usage.setUsageClass(rs.getObject(19) == null ? "" :  rs.getObject(19).toString());
		
		return usage;
	}

}
