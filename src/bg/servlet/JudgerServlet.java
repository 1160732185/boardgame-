package bg.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bg.pojo.Competition;
import bg.pojo.Game;
import bg.pojo.Judger;
import bg.pojo.Player;
import bg.pojo.Scoring;
import bg.pojo.Table;
import bg.pojo.User;
import bg.service.JudgerService;
import bg.service.PlayerService;
import bg.service.impl.JudgerServiceImpl;
import bg.service.impl.PlayerServiceImpl;

public class JudgerServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String oper=req.getParameter("oper");
		if(oper.equals("login")){checkJudgerLogin(req,resp);}
		else if(oper.equals("supervise")){judgerSupervise(req,resp);}
		else if(oper.equals("dosupervise")){judgerDosupervise(req,resp);}
		else if(oper.equals("score")){judgerScore(req,resp);}
		else if(oper.equals("doscore")){judgerDoScore(req,resp);}
		else{System.out.println("û���ҵ���Ӧ������"+oper);}
	}

	private void judgerDoScore(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		JudgerService js=new JudgerServiceImpl();
	    //��ȡ��Ϸ��
	    String pnos=req.getParameter("pno");
	    String tano=req.getParameter("tano");
	    String scores= req.getParameter("score");
	    int pno=Integer.parseInt(pnos);
	    int score=Integer.parseInt(scores);
	    System.out.println(pno+tano+score);
		int index=js.JudgerScoreService(pno,tano,score);
		try {
			resp.sendRedirect("judger?oper=score");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		
	}

	private void judgerDosupervise(HttpServletRequest req,HttpServletResponse resp) {
		// TODO Auto-generated method stub
		JudgerService js=new JudgerServiceImpl();
		HttpSession session=req.getSession();//��ȡsession
	    Object j=session.getAttribute("judger");
	    //��ȡ��Ϸ��
	    String tano=req.getParameter("tano");
	    int jno=((Judger) j).getJno();
	    System.out.println(tano+jno);
		int index=js.TableSuperviseService(jno,tano);
		try {
			resp.sendRedirect("judger?oper=supervise");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
	

	private void judgerScore(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		JudgerService js=new JudgerServiceImpl();
		HttpSession session=req.getSession();//��ȡsession
	    Object j=session.getAttribute("judger");
	    int jno=((Judger) j).getJno();
	    List<Scoring> ls=js.ScoringService(jno);
	    req.setAttribute("ls", ls);
	    try {
			req.getRequestDispatcher("/judger/score.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	private void judgerSupervise(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		JudgerService js=new JudgerServiceImpl();	
		List<Table> lt=js.TableSuperviseService();
			req.setAttribute("lt", lt);
			try {
				req.getRequestDispatcher("/judger/supervise.jsp").forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ;
		
		
	}

	private void checkJudgerLogin(HttpServletRequest req,HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String jid=req.getParameter("jid");
		String jpassword=req.getParameter("jpassword");
		System.out.println(jid+":"+jpassword);
	    JudgerService js=new JudgerServiceImpl();
	    //У��
	    Judger j=js.checkJudgerLoginService(jid, jpassword);
	    if(j!=null){ 
	    	//��ȡsession
	    	HttpSession hs=req.getSession();
	    	hs.setAttribute("judger",j);
	    	//�ض���
	    	try {
				resp.sendRedirect("/BG/main/main.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }else{
	    	//��¼ʧ�ܣ�����ת����
	    	try {
	    		//��Ӵ����ʶ
	    		req.setAttribute("error", 0);
				try {
					req.getRequestDispatcher("/Login.jsp").forward(req, resp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}
