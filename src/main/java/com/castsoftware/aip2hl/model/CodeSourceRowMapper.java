package com.castsoftware.aip2hl.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CodeSourceRowMapper implements RowMapper<CodeSourceFolderDTO> {

	@Override
	public CodeSourceFolderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		CodeSourceFolderDTO csfd = new CodeSourceFolderDTO();
		
		csfd.setObjectId(rs.getInt("object_id"));
		csfd.setServerPath(rs.getString("serverpath"));
		csfd.setDeployPath(rs.getString("deploypath"));
		
		return csfd;
	}

}
