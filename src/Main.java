import javax.swing.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
class Branch
{
	String branchName="";
	int branchlocation=0;
	public Branch(String branchName,int branchlocation)
	{
		this.branchName=branchName;
		this.branchlocation=branchlocation;
	}
}
public class Main
{
	int[] memory = new int[1000];
	int[] register = new int[32];
	ArrayList<Branch> branches= new ArrayList<Branch>();
	ArrayList<String> instSet= new ArrayList<String>(Arrays.asList("ADD","ADDI","BEQ","SUB","load","store"));
	int pc=0;
	String path;
	public Main(String path)
	{
		this.path = path;
		Arrays.fill(memory, -100);
		Arrays.fill(register, -100);
		register[0]=0;
	}
	public void printNonEmpty(int[] array)
	{
		for (int i = 0; i < array.length; i++) {
			if (array[i] != -100)
				System.out.println("At index " + i + " Value: " + array[i]);
		}
	}
	public int getRegisterIndex(String registerName)
	{
		int result = 0;
		switch (registerName) {
			case "$zero":
				break;
			case "$at":
				result = 1;
				break;
			case "$v0":
				result = 2;
				break;
			case "$v1":
				result = 3;
				break;
			case "$a0":
				result = 4;
				break;
			case "$a1":
				result = 5;
				break;
			case "$a2":
				result = 6;
				break;
			case "$a3":
				result = 7;
				break;
			case "$t0":
				result = 8;
				break;
			case "$t1":
				result = 9;
				break;
			case "$t2":
				result = 10;
				break;
			case "$t3":
				result = 11;
				break;
			case "$t4":
				result = 12;
				break;
			case "$t5":
				result = 13;
				break;
			case "$t6":
				result = 14;
				break;
			case "$t7":
				result = 15;
				break;
			case "$s0":
				result = 16;
				break;
			case "$s1":
				result = 17;
				break;
			case "$s2":
				result = 18;
				break;
			case "$s3":
				result = 19;
				break;
			case "$s4":
				result = 20;
				break;
			case "$s5":
				result = 21;
				break;
			case "$s6":
				result = 22;
				break;
			case "$s7":
				result = 23;
				break;
			case "$t8":
				result = 24;
				break;
			case "$t9":
				result = 25;
				break;
			default:
				System.out.print("unrecognized register please enter a valid register name and make sure it it begins with an $ ");
		}
		return result;
	}
	public void runInstruction(ArrayList<String> program)
	{
		for(int i =0;i<program.size();i++)
		{
			String entireLine = program.get(i);
			String[] split = entireLine.split(":");
			if(split.length==2)
			{
				Branch branch =new Branch(split[0],i);
				branches.add(branch);
				System.out.println(branch.branchName);
				System.out.println(branch.branchlocation);
			}
			else if(split.length==1)
			{
				if(!(instSet.contains(split[0].split(" ")[0])))
				{
					Branch branch =new Branch(split[0],i);
					branches.add(branch);
					System.out.println(branch.branchName);
					System.out.println(branch.branchlocation);
				}
			}

		}
		boolean branctemp=false;
		for (int i =0;i<program.size();i++)
		{
			if(branctemp){
				i=pc;
				branctemp=false;
			}
			String entireLine = program.get(i);
			String[] split = entireLine.split(":");
			String instruction="";
			if(split.length==2)
			{
				instruction = split[1];
			}
			else
			{
				instruction = split[0];
			}
			String[] parts = instruction.split(" ");
			if(parts.length==1){
				continue;
			}
			String[] operands = parts[1].split(",");
			int indexOutput;
			int indexOp1;
			int indexOp2;
			boolean operandSwitch = false;
			String operand1 = "";
			String operand2 = "";
			switch (parts[0]) {
				case "ADD":
					indexOutput = getRegisterIndex(operands[0]);
					indexOp1 = getRegisterIndex(operands[1]);
					indexOp2 = getRegisterIndex(operands[2]);
					register[indexOutput] = register[indexOp1] + register[indexOp2];
					printNonEmpty(register);
					break;
				case "ADDI":
					indexOutput = getRegisterIndex(operands[0]);
					indexOp1 = getRegisterIndex(operands[1]);
					try {
						int immediate = Integer.parseInt(operands[2]);
						register[indexOutput] = register[indexOp1] + immediate;
						printNonEmpty(register);
					} catch (Exception e) {
						System.out.println("unknown data type entered in the immediate value please enter a integer");
						System.exit(1);
					}
					break;
				case "SUB":
					indexOutput = getRegisterIndex(operands[0]);
					indexOp1 = getRegisterIndex(operands[1]);
					indexOp2 = getRegisterIndex(operands[2]);
					register[indexOutput] = register[indexOp1] - register[indexOp2];
					printNonEmpty(register);
					break;
				case "load"://load $s1,8($s3)
					for (int j = 0; j < operands[1].length(); j++) {
						char current = operands[1].charAt(j);
						if (current == '(' || current == ')') {
							operandSwitch = true;
						} else if (!operandSwitch) {
							operand1 = (String) operand1 + current;
						} else {
							operand2 = (String) operand2 + current;
						}
					}
					indexOutput = getRegisterIndex(operands[0]);
					indexOp1 = Integer.parseInt(operand1);
					indexOp2 = getRegisterIndex(operand2);
					register[indexOutput] = memory[ register[indexOp2] + indexOp1];
					printNonEmpty(register);
					break;
				case "store":
					for (int j = 0; j < operands[1].length(); j++) {
						char current = operands[1].charAt(j);
						if (current == '(' || current == ')') {
							operandSwitch = true;
						} else if (!operandSwitch) {
							operand1 = (String) operand1 + current;
						} else {
							operand2 = (String) operand2 + current;
						}
					}
					indexOutput = getRegisterIndex(operands[0]);
					indexOp1 = Integer.parseInt(operand1);
					indexOp2 = getRegisterIndex(operand2);
					memory[indexOp1 + register[indexOp2]] = register[indexOutput];
					printNonEmpty(memory);
					break;
				case "BEQ":
					indexOutput = getRegisterIndex(operands[0]);
					indexOp1 = getRegisterIndex(operands[1]);
					if(register[indexOutput]==register[indexOp1])
					{
						for(Branch branch : branches)
						{
							if(branch.branchName.equals(operands[2]))
							{
								pc = branch.branchlocation;
								branctemp=true;
							}
						}
						if(!branctemp)
						{
							System.out.print("unknown branch name");
							System.exit(1);
						}
					}
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + parts[0]);
			}
			System.out.println("--------Instruction Done---------");
		}
	}
	public void runProgram()
	{
		ArrayList<String> program= new ArrayList<String>();
		try
		{
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine())
			{
				String inst = myReader.nextLine();
				program.add(inst);
			}
			myReader.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		runInstruction(program);
	}
	public static void main(String[] args)
	{
		Main run = new Main("program.txt");
		run.runProgram();
	}
}
