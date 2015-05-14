The two xml files within this folder are Ant build scripts to execute test
scenarios that cover RTT's main use cases. Each scenario generates a test
archive, manipulates it and exports its log, which can be consulted to check
if RTT behaved as expected. The two build files test the very same scenarios
using RTT's command line and Ant user interface respectively.

The scenarios depend on test cases contained in subfolders of the
"testsets" folder. Each subfolder's name describes the result of each
of its test cases, whereas each literal represents one test case, starting
from first to last. The subfolders' naming scheme is:
 - c: correct test case
 - f: test case that fails
 - e: test case that does not work, i.e. aborts with an exception

Scenarios' results will be generated into subfolders of the
"testresults" folder, whereas each subfolder is named like the scenario it
represents. Similar like test cases, the scenarios have a naming scheme:
 - "ga" (Generate Archive): An "o" following the "ga" means "the overwrite
   flag is true". It follows the default configuration. E.g. "ga_o_1" means
   "generate the archive with overwrite flag true and default configuration
   1".
 - "uc" (Update Configuration): The configuration to update follows. An "o"
   following the "uc" means "the overwrite flag is true". A "s" following
   the configuration to update means the update is skipped, either since
   the configuration did not change or the overwrite flag was not set. E.g.
   "uc_o_1s" means "update the configuration 1, whereas the overwrite flag
   is set but nethertheless the update will be skipped because the
   configuration did not change".
 - "ua" (Update Archive): The test set to use for update follows. The test
   suite to update follows by "s" and the suite's number. Then the test
   cases that are updated follow, whereas an "s" means the test case is
   not updated - i.e. it is skipped - and an "r" means the test case is
   removed. E.g. "ua_o_ccc_s1_1s23" means "update the test cases 1, 2 and 3
   of test suite 1 with the test set ccc, whereas the overwrite flag is set
   and the 1'th test case's update is skipped since it did not change at
   all".
 - "ur" (Update Results): Again the configuration to use and the test suite
   and test cases to update follow, but each test case can be followed by
   an "e" which means "the generation of the results of the respective
   test case fails with an exception" or an "s" which means "the generation
   of new results is skipped since the result did not change". E.g.
   "ur_c1_s2_34e7" means "update the results of test suite 2 using
   configuration 1. The update generates results for test cases 3, 4 and 7,
   whereas the generation for test case 4 fails with an exception".
 - "rt" (Run Tests): Again the configuration to use and the test suite and
   its respective test cases to execute follow, whereas each test case is
   augmented with an "c" if it is correct, "f" if it fails, "e" if it
   fails with an exception or "s" if it is skipped since its results are
   not up to date. E.g. "rt_c3_s2_2c9s3c" means "run the test cases 2, 9
   and 3 of test suite 2 using configuration 3, whereas test cases 2 and
   3 succeed and test case 9 is skipped".
