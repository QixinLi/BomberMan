public class SqStack{
	 
    private Path[] stackElem;
    private int top;
 
    public SqStack(int maxSize)
    {
        stackElem=new Path[maxSize];
        top=0;
    }
 
    //清空
    public void clear()
    {
        top=0;
    }
    //是否为空
    public boolean isEmpty()
    {
        return top==0;
    }
    //元素个数
    public int length()
    {
        return top;
    }
    //栈顶
    public Path peek()
    {
        if(!isEmpty())
            return stackElem[top-1];
        else
            return null;
    }
 
    //入栈
    public void push(Path x) throws Exception
    {
        if(top==stackElem.length)
        {
            throw new Exception("栈已满！");
        }
        else
        {
            stackElem[top++]=x;
        }
    }
    //出栈
    public Path pop() throws Exception
    {
        if(top==0)
        {
            throw new Exception("栈为空！");
        }
        else
            return stackElem[--top];  //删除然后返回现在的栈顶
    }
 
    //打印（从栈顶到栈底）
    public void display()
    {
        for(int i=length()-1; i>=0; i--)
        {
            System.out.print(stackElem[i]+" ");
        }
        System.out.println();
    }
}
