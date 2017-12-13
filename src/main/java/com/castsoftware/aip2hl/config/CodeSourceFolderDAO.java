package com.castsoftware.aip2hl.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.castsoftware.aip2hl.model.CodeSourceFolderDTO;
import com.castsoftware.aip2hl.model.CodeSourceRowMapper;

@Service
public class CodeSourceFolderDAO {
	@Autowired private JdbcTemplate jdbcTemplate;

	private static final String QUERY_FIND_SOURCE = "SELECT object_id, serverpath, deploypath FROM mngt_xxxx.cms_pref_sources";

	public List<CodeSourceFolderDTO> getSource(String mngtSchema) {
		String qry = QUERY_FIND_SOURCE.replace("mngt_xxxx", mngtSchema); 
		return jdbcTemplate.query(qry,new CodeSourceRowMapper());
	}

}
