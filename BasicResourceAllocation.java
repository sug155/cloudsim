mport org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import java.util.*;
public class BasicResourceAllocation {
public static void main(String[] args) {
// Initialize the CloudSim library
int numUsers = 1;
Calendar calendar = Calendar.getInstance();
boolean traceFlag = false;
CloudSim.init(numUsers, calendar, traceFlag);
// Create Datacenter
Datacenter datacenter = createDatacenter("Datacenter_0");
// Create Broker
DatacenterBroker broker = createBroker();
int brokerId = broker.getId();
// Create VMs
List<Vm> vmList = new ArrayList<>();
int vmCount = 4; // Number of VMs
for (int i = 0; i < vmCount; i++) {
Vm vm = new Vm(i, brokerId, 1000, 1, 1024, 1000, 10000, "Xen", new
CloudletSchedulerTimeShared());
vmList.add(vm);
}
// Submit VMs to broker
broker.submitVmList(vmList);
// Create Cloudlets
List<Cloudlet> cloudletList = new ArrayList<>();
int cloudletCount = 8; // Number of Cloudlets
for (int i = 0; i < cloudletCount; i++) {
Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300, new UtilizationModelFull(),
new UtilizationModelFull(), new UtilizationModelFull());
cloudlet.setUserId(brokerId);
cloudletList.add(cloudlet);
}
// Submit Cloudlets to broker
broker.submitCloudletList(cloudletList);
// Start Simulation
CloudSim.startSimulation();
// Stop Simulation
CloudSim.stopSimulation();
// Print results
List<Cloudlet> resultList = broker.getCloudletReceivedList();
printCloudletResults(resultList);
}
private static Datacenter createDatacenter(String name) {
List<Host> hostList = new ArrayList<>();
int hostCount = 4; // Number of Hosts
for (int i = 0; i < hostCount; i++) {
int hostId = i;
int ram = 2048; // Host RAM
long storage = 1000000; // Host storage
int bw = 10000; // Host bandwidth
List<Pe> peList = new ArrayList<>();
peList.add(new Pe(0, new PeProvisionerSimple(1000))); // Host CPU cores
Host host = new Host(hostId, new RamProvisionerSimple(ram), new
BwProvisionerSimple(bw), storage, peList,
new VmSchedulerTimeShared(peList));
hostList.add(host);
}
String arch = "x86"; // architecture
String os = "Linux"; // operating system
String vmm = "Xen"; // virtual machine monitor
double time_zone = 10.0; // time zone this resource located
double cost = 3.0; // the cost of using processing in this resource
double costPerMem = 0.05; // the cost of using memory in this resource
double costPerStorage = 0.001; // the cost of using storage in this resource
double costPerBw = 0.0; // the cost of using bw in this resource
DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os,
vmm, hostList, time_zone,
cost, costPerMem, costPerStorage, costPerBw);
Datacenter datacenter = null;
try {
datacenter = new Datacenter(name, characteristics, new
VmAllocationPolicySimple(hostList), new LinkedList<>(), 0);
} catch (Exception e) {
e.printStackTrace();
}
return datacenter;
}
private static DatacenterBroker createBroker() {
DatacenterBroker broker = null;
try {
broker = new DatacenterBroker("Broker");
} catch (Exception e) {
e.printStackTrace();
}
return broker;
}
private static void printCloudletResults(List<Cloudlet> list) {
String indent = " ";
System.out.println("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" +
indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
for (Cloudlet cloudlet : list) {
System.out.print(indent + cloudlet.getCloudletId() + indent + indent);
if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
System.out.println("SUCCESS" + indent + indent + cloudlet.getResourceId() +
indent + indent + cloudlet.getVmId() + indent + indent
+ cloudlet.getActualCPUTime() + indent + cloudlet.getExecStartTime() + indent
+ indent + cloudlet.getFinishTime());
}
}
}
}

