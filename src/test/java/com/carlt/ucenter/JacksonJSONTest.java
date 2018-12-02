package com.carlt.ucenter;

import java.util.Arrays;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.carlt.ucenter.domain.AuthChangeMsg;
import com.carlt.ucenter.util.Utils;

public class JacksonJSONTest {

	public static void main(String[] args) {

		Jackson2JsonRedisSerializer<? extends Object> serializer = Utils.getJsonSerializer();

		AuthChangeMsg msg = new AuthChangeMsg("user", "linkRole", Arrays.asList("1", "1,2,3,4"));

		byte[] b = serializer.serialize(msg);

		System.out.println(new String(b));

		Object ret = serializer.deserialize(b);
		AuthChangeMsg authmsg = null;
		if (ret instanceof AuthChangeMsg)
			authmsg = (AuthChangeMsg) ret;

		System.out.println(authmsg.getData());
		System.out.println(authmsg.getOperate());
		System.out.println(authmsg.getType());
		System.out.println();

	}
}
