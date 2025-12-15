import java.util.Stack;
class MinStack{
    private Stack<Integer> stack;
    private Stack<Integer> minStack;
    public MinStack(){
        stack = new Stack<>();
    }
    public void push(int val){
        stack,push(val);
        if(minStack.isEmpty() || val <= minStack.peek()){
            minStack,push(val);
        }
    }
    public void pop(){
        if(!stack.isEmpty()){
            if (!stack.isEmpty())
            
        }
    }
}