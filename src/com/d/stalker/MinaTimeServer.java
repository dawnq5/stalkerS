package com.d.stalker;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionInitializer;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.alibaba.fastjson.JSON;
import com.d.stalker.bean.Message;


public class MinaTimeServer {
	private static final int PORT = 9123;//定义监听端口 
	public static void main( String[] args ) throws IOException{ 
		IoAcceptor acceptor = new NioSocketAcceptor(); 
		acceptor.getFilterChain().addLast( "logger", new LoggingFilter() ); 
		acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));//指定编码过滤器 
		 
		 
		acceptor.setHandler(new IoHandlerAdapter(){
			public void sessionCreated(IoSession session) {
				// 显示客户端的ip和端口
				Collection<IoSession> sessions = session.getService().getManagedSessions().values();
				
				System.out.println("IP："+session.getRemoteAddress().toString()+",sessionId:"+session.getId());
				System.out.println("用户上线，"+"在线人数=》"+sessions);
			}

			@Override
			public void messageReceived( IoSession session, Object message) throws Exception {
				String str = message.toString();
				System.out.println("server收到消息："+str);
				if (str.trim().equalsIgnoreCase("quit")) {
					session.close(true);// 结束会话
					return;
				}else{
				Message mes=JSON.parseObject(message.toString(), Message.class);
				if("login".equals(mes.getCmd())){
					session.setAttribute("userName", mes.getFormUser());
					return;
				}
				
				Collection<IoSession> sessions = session.getService().getManagedSessions().values();
				System.out.println("来自"+session.getAttribute("userName")+"的消息。"+sessions.size());	
				
				String userName=mes.getToUser();
				
					
					if("all".equals(mes.getToUser())){
						for (IoSession sess : sessions) {
					       sess.write(mes.getMessage());
						}
					}else{
						 for (IoSession sess : sessions) {
							 if(userName.equals(sess.getAttribute("userName"))){
						       sess.write(mes.getMessage());
						       break;
							 }
							}
							 
						 }
				session.write("back"); 
				}
				
			}
		});//指定业务逻辑处理器 
		acceptor.setDefaultLocalAddress( new InetSocketAddress(PORT) );//设置端口号 
		acceptor.bind();//启动监听 
	}
}
