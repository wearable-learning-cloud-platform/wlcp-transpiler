package org.wlcp.wlcptranspiler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.feignclient.GameFeignClient;
import org.wlcp.wlcptranspiler.serivce.impl.JavaScriptTranspilerServiceImpl;

@Controller
@RequestMapping("/transpilerController")
public class TranspilerController {
	
	@Autowired
	private GameFeignClient gameFeignClient;
	
	@Autowired
	private JavaScriptTranspilerServiceImpl javaScriptTranspilerServiceImpl;
	
	@Value("${security.jwt-token}")
	private String jwtToken;

	@GetMapping(value="/transpileGame", produces="application/javascript")
	public ResponseEntity<String> transpile(@RequestParam("gameId") String gameId, @RequestParam("archivedGame") boolean archivedGame) {
		GameDto gameDto = null;
		if(!archivedGame) {
			gameDto = gameFeignClient.getGame(gameId, jwtToken);
		} else {
			gameDto = gameFeignClient.getArchivedGame(gameId, jwtToken);
		}
		return new ResponseEntity<String>(javaScriptTranspilerServiceImpl.transpile(gameDto), HttpStatus.OK);
	}
}
