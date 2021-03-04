import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

class Soldier {
    String name;
    long score;
    int pos;

    public Soldier(String name, long score){
        this.name = name;
        this.score = score;

    }

    public Soldier(String name, long score, int pos){
        this.name = name;
        this.score = score;
        this.pos = pos;

    }
}

class MinHeap {
    Soldier[] heap;
    int size;
    int max;

    public MinHeap(int max){
        this.max = max;
        this.size = 0;
        this.heap = new Soldier[this.max + 1];
        heap[0] = new Soldier("Dummy", 0, 0); //Index the heap at 1, not 0
    }

    public void insert(Soldier soldier){

        if(size >= max) return;

        size++;

        soldier.pos = size;

        heap[size] = soldier;

        floatUp(size);

    }

    public Soldier remove(){

        Soldier toReturn = peek();

        swap(1, size);

        heap[size] = null;

        size--;

        sinkDown(1);

        return toReturn;


    }

    public Soldier peek(){ //return heap[1]

        return heap[1];
    }

    private void floatUp(int pos){ //recursive float up

        if(heap[pos].score < heap[parent(pos)].score){
            swap(pos, parent(pos));
            floatUp(parent(pos));
        } else {
            return;
        }


    }

    public void sinkDown(int pos){ //recursive sink down function

        if(!noChildren(pos)){

            if(heap[left(pos)] != null && heap[right(pos)] != null){
                if(heap[pos].score > heap[left(pos)].score || heap[pos].score > heap[right(pos)].score){

                    if(heap[left(pos)].score > heap[right(pos)].score){
                        swap(pos, right(pos));
                        sinkDown(right(pos)); //go to right

                    } else {
                        swap(pos, left(pos));
                        sinkDown(left(pos)); //go to left

                    }
                }
            } else if(heap[right(pos)] == null){ //this means right child is null
                if(heap[pos].score > heap[left(pos)].score){
                    swap(pos, left(pos));
                    sinkDown(left(pos));
                }
            } else {
                if(heap[pos].score > heap[right(pos)].score){
                    swap(pos, right(pos));
                    sinkDown(right(pos));
                }
            }

        }


    }

    private void swap(int pos1, int pos2){

        Soldier temp = heap[pos1];

        heap[pos1] = heap[pos2];
        heap[pos1].pos = pos1; //update positions

        heap[pos2] = temp;
        heap[pos2].pos = pos2; //update positions
    }

    private int left(int pos){
        return 2 * pos;
    }

    private int right(int pos){
        return (2 * pos) + 1;
    }

    private int parent(int pos){
        return pos/2;
    }

    private boolean noChildren(int pos) { //when logic is reversed, doesn't work for some reason
        if (pos > (size / 2) && pos <= size) {
            return true;
        }
        return false;
    }

    public void print(){
        for (int i = 1; i <= size / 2; i++) {
            String rootName = heap[i].name;
            long rootScore = heap[i].score;

            String leftName = heap[left(i)] != null ? heap[left(i)].name : "Null";
            long leftScore = heap[left(i)] != null ? heap[left(i)].score : 0;

            String rightName = heap[right(i)] != null ? heap[right(i)].name : "Null";
            long rightScore = heap[right(i)] != null ? heap[right(i)].score : 0;

            System.out.print(" Parent : " + rootName + " " + rootScore +
                    " Left Child : " + leftName + " " + leftScore
                    + " Right Child :" + rightName + " " + rightScore);
            System.out.println();
        }
    }

}


public class Spartan {

    public static void main(String[] args) throws IOException{

        HashMap<String, Soldier> map = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);

        int size = scanner.nextInt();

        MinHeap minheap = new MinHeap(size + 1);

        for(int i = 0; i < size; i++){
            String name = scanner.next();
            long score = scanner.nextInt();

            Soldier soldier = new Soldier(name, score);

            map.put(name, soldier);
            minheap.insert(soldier);
        }

        int queries = scanner.nextInt();

        for(int i = 0; i < queries; i++){

            int queryType = scanner.nextInt();

            if(queryType == 1){ //just update the score of soldier and access thru hashmap
                String name = scanner.next();
                long delta = scanner.nextLong();
                map.get(name).score += delta;
                minheap.sinkDown(map.get(name).pos);

            } else { //queryType 2
                long minimum = scanner.nextLong();

                while(minheap.peek().score < minimum){
                    minheap.remove();
                }

                output.write(minheap.size + "\n");


            }
        }

        scanner.close();
        output.flush();



    }
}
