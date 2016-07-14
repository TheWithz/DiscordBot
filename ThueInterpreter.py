import os
from string import whitespace

import sys


# removes whitespace including tabs, line breaks, and spaces
def remove_whitespace(x):
    for c in whitespace:
        x = x.replace(c, "")
    return x


# converts the raw code string into a list of rules, each of which is in the
# form [lhs, rhs]
def string_to_code(raw_code):
    try:
        if raw_code[-1] != ";":
            return None
        return [i.split("=") for i in remove_whitespace(raw_code[:-1]).split(";")]
    except:
        return None


#

def pad_right(text, length=50):
    return text + (' ' * (length - len(text)))


# run the actual program with given rules, an initial state
# the correct output that is desired, the maximum number of
# steps the computation should be run for, and the maximum size of
# the state string
def run_program(rules, input_state, max_steps=1000000, max_size=1000000, print_steps=True):
    with open('trace.thue', 'w') as f:
        f.write(input_state + '\n')
        if rules is None:
            return {"correct": False, "error": "Program threw an error"}
        state = remove_whitespace(input_state)

        for i in range(max_steps):
            rule_found = False
            for [k, v] in rules:
                if k in state:
                    state = state.replace(k, v, 1)
                    rule_found = True
                    f.write(pad_right(state) + pad_right(k, 5) + ' -> ' + pad_right(v, 5) + '\n')
                    if print_steps:
                        print(" " + state)
                    break

            if len(state) > max_size:
                return {"correct": False, "error": "Size limit exceeded"}

            if not rule_found:
                return state

        return {"correct": False, "error": "Program threw an error"}


#

if __name__ == "__main__":
    content = ""
    if os.path.isfile("file.thue"):
        with open('file.thue', 'r') as content_file:
            content = content_file.read()
    else:
        content = sys.argv[1]

    rules, b, states = content.partition(":::")
    rules = rules.strip()
    states = [x.strip() for x in states.split("\n")]

    for state in states:
        if state == "":
            continue
        print(run_program(string_to_code(rules), state, print_steps=(sys.argv[2] == "True" or sys.argv[2] == "true")))
        print()
        #
