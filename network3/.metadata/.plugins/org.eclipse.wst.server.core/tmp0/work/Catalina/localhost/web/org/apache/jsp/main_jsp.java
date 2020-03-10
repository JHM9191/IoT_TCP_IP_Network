/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.30
 * Generated at: 2020-03-09 09:38:14 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class main_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSP들은 오직 GET, POST 또는 HEAD 메소드만을 허용합니다. Jasper는 OPTIONS 메소드 또한 허용합니다.");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta charset=\"UTF-8\">\n");
      out.write("<title>Insert title here</title>\n");
      out.write("<style>\n");
      out.write("@import url('https://fonts.googleapis.com/css?family=Roboto+Condensed');\n");
      out.write("\n");
      out.write(".preserve-3d {\n");
      out.write("\ttransform-style: preserve-3d;\n");
      out.write("\t-webkit-transform-style: preserve-3d;\n");
      out.write("}\n");
      out.write("\n");
      out.write("body {\n");
      out.write("\tbackground-color: #EAEAEA;\n");
      out.write("\talign-content: center;\n");
      out.write("\t/* vertical-align: middle; */\n");
      out.write("\tpadding: 0;\n");
      out.write("\tpadding-top: 100px;\n");
      out.write("\tpadding-bottom: 100px;\n");
      out.write("\tmargin: 0;\n");
      out.write("\tborder: 0;\n");
      out.write("\toverflow-x: none;\n");
      out.write("\tfont-family: Roboto Condensed, sans-serif;\n");
      out.write("\tfont-size: 40px;\n");
      out.write("\tfont-smooth: always;\n");
      out.write("\t-webkit-font-smoothing: antialiased;\n");
      out.write("\tposition: relative;\n");
      out.write("\tfont-family: Roboto Condensed, sans-serif;\n");
      out.write("\tmargin: 0;\n");
      out.write("}\n");
      out.write("\n");
      out.write("/* table */\n");
      out.write("th {\n");
      out.write("\twidth: 180px;\n");
      out.write("\tcolor: gray;\n");
      out.write("\tborder-right: solid;\n");
      out.write("\tborder-right-width: 1px;\n");
      out.write("\tborder-right-color: gray;\n");
      out.write("}\n");
      out.write("\n");
      out.write("td {\n");
      out.write("\tpadding-left: 20px;\n");
      out.write("}\n");
      out.write("\n");
      out.write("table {\n");
      out.write("\tborder: solid;\n");
      out.write("\tborder-width: 1px;\n");
      out.write("\tborder-color: black;\n");
      out.write("\tbackground: black;\n");
      out.write("\tpadding: 20px;\n");
      out.write("\tbackground-color: white;\n");
      out.write("}\n");
      out.write("\n");
      out.write("select, option {\n");
      out.write("\tfont-family: Roboto Condensed, sans-serif;\n");
      out.write("\tfont-size: 20px;\n");
      out.write("}\n");
      out.write("\n");
      out.write("/* button customization */\n");
      out.write(".button_base {\n");
      out.write("\tmargin: 0;\n");
      out.write("\tborder: 0;\n");
      out.write("\tfont-size: 18px;\n");
      out.write("\tposition: relative;\n");
      out.write("\ttop: 50%;\n");
      out.write("\tleft: 50%;\n");
      out.write("\tmargin-top: -25px;\n");
      out.write("\tmargin-left: -100px;\n");
      out.write("\twidth: 200px;\n");
      out.write("\theight: 50px;\n");
      out.write("\ttext-align: center;\n");
      out.write("\tcursor: default;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".button_base:hover {\n");
      out.write("\tcursor: pointer;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".btn_3d_pushback {\n");
      out.write("\tperspective: 500px;\n");
      out.write("\t-webkit-perspective: 500px;\n");
      out.write("\t-moz-perspective: 500px;\n");
      out.write("\ttransform-style: preserve-3d;\n");
      out.write("\t-webkit-transform-style: preserve-3d;\n");
      out.write("\t-moz-perspective: 500px;\n");
      out.write("\ttransform-style: preserve-3d;\n");
      out.write("\ttransform-style: preserve-3d;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".btn_3d_pushback div {\n");
      out.write("\tposition: absolute;\n");
      out.write("\ttext-align: center;\n");
      out.write("\twidth: 100%;\n");
      out.write("\theight: 50px;\n");
      out.write("\tbox-sizing: border-box;\n");
      out.write("\t-webkit-box-sizing: border-box;\n");
      out.write("\t-moz-box-sizing: border-box;\n");
      out.write("\tpointer-events: none;\n");
      out.write("\tpadding: 10px;\n");
      out.write("\tborder: #000000 solid 1px;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".btn_3d_pushback div:nth-child(1) {\n");
      out.write("\tcolor: #000000;\n");
      out.write("\tbackground-color: #ffffff;\n");
      out.write("\ttransform: rotateX(0deg) translateZ(0px);\n");
      out.write("\t-webkit-transform: rotateX(0deg) translateZ(0px);\n");
      out.write("\t-moz-transform: rotateX(0deg) translateZ(0px);\n");
      out.write("\ttransform-origin: 50% 50%;\n");
      out.write("\t-webkit-transform-origin: 50% 50%;\n");
      out.write("\t-moz-transform-origin: 50% 50%;\n");
      out.write("\ttransition: all 0.2s ease;\n");
      out.write("\t-webkit-transition: all 0.2s ease;\n");
      out.write("\t-moz-transition: all 0.2s ease;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".btn_3d_pushback div:nth-child(2) {\n");
      out.write("\tcolor: #ffffff;\n");
      out.write("\tbackground-color: #000000;\n");
      out.write("\ttransform: rotateX(-179.5deg) translateZ(1px);\n");
      out.write("\t-webkit-transform: rotateX(-179.5deg) translateZ(1px);\n");
      out.write("\t-moz-transform: rotateX(-179.5deg) translateZ(1px);\n");
      out.write("\ttransform-origin: 50% 50%;\n");
      out.write("\t-webkit-transform-origin: 50% 50%;\n");
      out.write("\t-moz-transform-origin: 50% 50%;\n");
      out.write("\ttransition: all 0.2s ease;\n");
      out.write("\t-webkit-transition: all 0.2s ease;\n");
      out.write("\t-moz-transition: all 0.2s ease;\n");
      out.write("}\n");
      out.write("\n");
      out.write(".btn_3d_pushback:hover div:nth-child(1) {\n");
      out.write("\ttransition: all 0.2s ease;\n");
      out.write("\t-webkit-transition: all 0.2s ease;\n");
      out.write("\t-moz-transition: all 0.2s ease;\n");
      out.write("\ttransform: rotateX(179.5deg);\n");
      out.write("\t-webkit-transform: rotateX(179.5deg);\n");
      out.write("\t-moz-transform: rotateX(179.5deg);\n");
      out.write("}\n");
      out.write("\n");
      out.write(".btn_3d_pushback:hover div:nth-child(2) {\n");
      out.write("\ttransition: all 0.2s ease;\n");
      out.write("\t-webkit-transition: all 0.2s ease;\n");
      out.write("\t-moz-transition: all 0.2s ease;\n");
      out.write("\ttransform: rotateX(0deg) rotateY(0deg) translateZ(1px);\n");
      out.write("\t-webkit-transform: rotateX(0deg) rotateY(0deg) translateZ(1px);\n");
      out.write("\t-moz-transform: rotateX(0deg) rotateY(0deg) translateZ(1px);\n");
      out.write("}\n");
      out.write("</style>\n");
      out.write("<script>\n");
      out.write("\tfunction submitForm() {\n");
      out.write("\t\tdocument.getElementById(\"webapp_form\").submit();\n");
      out.write("\t}\n");
      out.write("</script>\n");
      out.write("</head>\n");
      out.write("<body topmargin=\"250dp\">\n");
      out.write("\t<form id=\"webapp_form\" action=\"webclient\" method=\"get\">\n");
      out.write("\t\t<div align=\"center\">\n");
      out.write("\t\t\t<h1>Admin Page</h1>\n");
      out.write("\t\t\t<table>\n");
      out.write("\t\t\t\t<tr>\n");
      out.write("\t\t\t\t\t<th>IP 주소</th>\n");
      out.write("\t\t\t\t\t<td colspan=\"2\"><select name=\"ip\" required=\"required\"\n");
      out.write("\t\t\t\t\t\tautofocus=\"autofocus\"\n");
      out.write("\t\t\t\t\t\tstyle=\"width: 450px; height: 40px; font-size: 20px;\">\n");
      out.write("\t\t\t\t\t\t\t<option value=\"\">ALL</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"/192.168.0.15\">IoT_Client_JHM</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"/192.168.43.2\">/192.168.43.2(IoT_Client_1)</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"IoT_Client_1\">IoT_Client_1(/192.168.43.2)</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"/192.168.0.6\">JMJ</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"/192.168.43.48\">/192.168.43.48(jmj)</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"jmj\">jmj(/192.168.43.48)</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"/70.12.231.236\">70.12.231.236</option>\n");
      out.write("\t\t\t\t\t\t\t<option value=\"/70.12.224.85\">70.12.224.85</option>\n");
      out.write("\n");
      out.write("\t\t\t\t\t</select></td>\n");
      out.write("\t\t\t\t</tr>\n");
      out.write("\t\t\t\t<tr>\n");
      out.write("\t\t\t\t\t<th>상태</th>\n");
      out.write("\t\t\t\t\t<td align=\"center\"><input type=\"radio\" name=\"state\" value=\"1\"\n");
      out.write("\t\t\t\t\t\tchecked=\"checked\" required=\"required\">start</td>\n");
      out.write("\t\t\t\t\t<td align=\"center\"><input type=\"radio\" name=\"state\" value=\"0\">stop</td>\n");
      out.write("\n");
      out.write("\t\t\t\t</tr>\n");
      out.write("\t\t\t\t<tr>\n");
      out.write("\t\t\t\t\t<th>메세지</th>\n");
      out.write("\t\t\t\t\t<td colspan=\"2\"><input type=\"text\"\n");
      out.write("\t\t\t\t\t\tstyle=\"height: 40px; font-size: 20px;\" size=\"40\" name=\"txt\"></td>\n");
      out.write("\t\t\t\t</tr>\n");
      out.write("\n");
      out.write("\t\t\t</table>\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<br> <br>\n");
      out.write("\t\t<div class=\"btn_3d_pushback button_base\" onClick=\"submitForm();\">\n");
      out.write("\t\t\t<div>보내기</div>\n");
      out.write("\t\t\t<div>보내기</div>\n");
      out.write("\t\t</div>\n");
      out.write("\t</form>\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
