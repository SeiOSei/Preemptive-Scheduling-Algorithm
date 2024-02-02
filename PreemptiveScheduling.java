import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Process {
    char processId;
    int arrivalTime;
    int burstTime;
    int priority;
    int remainingBurstTime;

    public Process(char processId, int arrivalTime, int burstTime, int priority) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingBurstTime = burstTime;
    }
}

public class PreemptiveScheduling {
    private static Scanner scan = new Scanner(System.in);
    private static int[] waitingTime;
    private static int[] turnaroundTime;
    private static int[] completionTime;
    public static void main(String[] args) {
        System.out.print("Enter the number of processes: ");
        int n = scan.nextInt();
        Process[] processes = new Process[n];

        for (char i = 'A'; i < 'A' + n; i++) {
            System.out.print("Process " + i + " (AT BT Priority): ");
            int arrivalTime = scan.nextInt();
            int burstTime = scan.nextInt();
            int priority = scan.nextInt();
            processes[i - 'A'] = new Process(i, arrivalTime, burstTime, priority);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
        waitingTime = new int[n];
        turnaroundTime = new int[n];
        completionTime = new int[n];

        //execute method calcus
        executePreemptiveAlgo(processes);

        //display results
        displayResult(processes);
    }

    static void executePreemptiveAlgo(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;

        while (completedProcesses < processes.length) {
            int selectedProcessIndex = findHighestPriorityProcessIndex(processes, currentTime);

            if (selectedProcessIndex == -1) {
                currentTime++;
            } else {
                Process selectedProcess = processes[selectedProcessIndex];
                if (selectedProcess.remainingBurstTime > 0) {
                    selectedProcess.remainingBurstTime--;
                    currentTime++;

                    if (selectedProcess.remainingBurstTime == 0) {
                        completionTime[selectedProcessIndex] = currentTime;
                        turnaroundTime[selectedProcessIndex] = completionTime[selectedProcessIndex] - selectedProcess.arrivalTime;
                        waitingTime[selectedProcessIndex] = turnaroundTime[selectedProcessIndex] - selectedProcess.burstTime;
                        completedProcesses++;
                    }
                }
            }
        }
    }

    static int findHighestPriorityProcessIndex(Process[] processes, int currentTime) {
        int highestPriority = Integer.MAX_VALUE;
        int lowestBurstTime = Integer.MAX_VALUE;
        int selectedProcessIndex = -1;
        for (int i = 0; i < processes.length; i++) {
            if (processes[i].arrivalTime <= currentTime &&
                processes[i].priority < highestPriority &&
                processes[i].remainingBurstTime > 0) {
                    highestPriority = processes[i].priority;
                    lowestBurstTime = processes[i].burstTime;
                    selectedProcessIndex = i;
            } else if (processes[i].arrivalTime <= currentTime &&
            processes[i].priority == highestPriority &&
            processes[i].remainingBurstTime > 0 &&
            processes[i].burstTime < lowestBurstTime) {
                lowestBurstTime = processes[i].burstTime;
                selectedProcessIndex = i;
            }
        }
        return selectedProcessIndex;
    }

    static void displayResult(Process[] processes) {
        System.out.println("Process\tAT\tBT\tP\tTAT\tWT\tCT");
        for (int i = 0; i < processes.length; i++) {
            System.out.println(processes[i].processId + "\t" + processes[i].arrivalTime + "\t" +
                    processes[i].burstTime + "\t" + processes[i].priority + "\t" + turnaroundTime[i] + "\t" + waitingTime[i] + "\t" + completionTime[i]);
        }
    }
}