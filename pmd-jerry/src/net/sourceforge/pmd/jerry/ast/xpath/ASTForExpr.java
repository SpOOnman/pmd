/* Generated By:JJTree: Do not edit this line. ASTForExpr.java */

package net.sourceforge.pmd.jerry.ast.xpath;

public class ASTForExpr extends SimpleNode {
  public ASTForExpr(int id) {
    super(id);
  }

  public ASTForExpr(XPath2Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(XPath2ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
