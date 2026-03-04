cmake_minimum_required(VERSION 3.15)

project(CoreBankingDemo
    VERSION 0.1.0
    LANGUAGES CXX
)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)


option(ENABLE_WARNINGS "Enable extra compiler warnings" ON)

if(ENABLE_WARNINGS)
    if(MSVC)
        add_compile_options(/W4)
    else()
        add_compile_options(-Wall -Wextra -Wpedantic)
    endif()
endif()

add_executable(corebank
    src/main.cpp
    src/AccountService.cpp
    src/Database.cpp
    src/Security.cpp
)

target_include_directories(corebank
    PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/src
)

