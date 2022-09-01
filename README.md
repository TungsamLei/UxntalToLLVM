# Uxntal to LLVM IR


This is a postgraduate student's final project of University of Glasgow.

As the title says, this project is to design and implement a compiler that could compile some basic Uxntal code to LLVM IR code.

## Project structure

### src file
#### LLVMGenerator
The classes in this package are responsible for compiling the code for each corresponding block to LLVM IR separately.

#### OperationType
List all the operations of Uxntal and some operations of LLVM IR.

#### Testing
Includes the testing files.

#### Tokens
7 types of tokens.

#### Utils
Read files and the tokens map.

#### root 
The Main class is used to check for errors during development.
The FinalLLVM class is used to run the whole project and to see the LLVM IR code output.




### Uxntal file

This file contains 24 Uxntal files given by my supervisor Wim Vanderbauwhede. 

This project could successfully convert ex01_0_simple_calc.tal, ex01_1_simple_calc.tal, ex01_2_simple_calc.tal,
ex01_3_simple_calc.tal, ex03_simple_calc_registers.tal, ex05_simple_calc_registers2, and ex06_subroutine-call
files into LLVM IR code.

## How to run this project
You can pull the code and run the FinalLLVM class to run this project. It would need you
to input the file name that you want to compile. For example, you should 
input "ex01_0_simple_calc" to compile the ex01_0_simple_calc.tal file. The result
of it would show in the command line window. It will appear behind a dividing line.

You can run the test files in the Testing package to check if it could pass the testing successfully.
