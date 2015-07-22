package com.d.stalker;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.alibaba.fastjson.JSON;
import com.d.stalker.bean.Message;


public class MinaTimeClient {
	public static void main(String[] args) {
		// 创建客户端连接器. 
		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
		connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" )))); //设置编码过滤器 
		connector.setConnectTimeoutMillis(1000*60);
		connector.setHandler(new IoHandlerAdapter(){
			public void messageSent(IoSession session, Object message) throws Exception {
				System.out.println("messageSent:"+message);
				//session.close(true);
			}
			public void messageReceived(IoSession session, Object message) throws Exception { 
				System.out.println("client收到消息："+message);//显示接收到的消息 
			}
		});//设置事件处理器 
		ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 9123));//建立连接 
		cf.awaitUninterruptibly();//等待连接创建完成 

		Message  message=new Message();
		message.setFormUser("user2");
		message.setMessage("登录");
		message.setCmd("login");//首次登录
		cf.getSession().write(JSON.toJSONString(message));
		
		//cf.getSession().write("hello");//发送消息 
		 
		cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开 
		connector.dispose(); 
	}
}
