cmake_minimum_required(VERSION 3.16)
project(server)

set(CMAKE_CXX_STANDARD 17)
find_package(Threads)

add_executable(server
        server.cpp Game.cpp Game.h GameManager.cpp GameManager.h server.h)
target_link_libraries( server ${CMAKE_THREAD_LIBS_INIT} )