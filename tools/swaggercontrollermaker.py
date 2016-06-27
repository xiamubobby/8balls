import os.path as ospath
import sys

# from . import CONTROLLER_PATH
PROJ_PATH = "../"
JAVA_SRC_PATH = ospath.join(PROJ_PATH, "src", "main", "java")
CONTROLLER_PATH = ospath.join(JAVA_SRC_PATH, "com", "knight", "controller")


SWAGGER_TEST_CONTROLLER_PATH = ospath.join(CONTROLLER_PATH, "swaggertest")

count = 16

try:
    count = int(sys.argv[1])
except ValueError:
    print("Stupid user, please enter a number")
    sys.exit(1)

filetext = "\n".join([
    "package com.knight.controller.swaggertest;",
    "import com.knight.dto.responsebody.UserInfo;",
    "import org.springframework.transaction.annotation.Transactional;",
    "import org.springframework.web.bind.annotation.RequestMapping;",
    "import org.springframework.web.bind.annotation.RequestParam;",
    "import org.springframework.web.bind.annotation.RestController;",
    "@RestController",
    "@RequestMapping(\"swagger{}\")",
    "public class swaggertest{} {{",
    "@Transactional",
    "@RequestMapping(\"register\")",
    "public UserInfo register(@RequestParam String account,@RequestParam String password) {{",
    "return new UserInfo();","}}","}}"

])

for i in range(0, count):
    idx = i + 1
    f = open(ospath.join(SWAGGER_TEST_CONTROLLER_PATH, "swaggertest{}.java".format(str(idx))), 'w', encoding="UTF-8")
    f.write(filetext.format(str(idx), str(idx)))
    f.close()