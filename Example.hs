{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use camelCase" #-}
module Example() where

import Foreign.C
import Foreign

foreign export ccall "incr" c_incr :: CInt -> CInt

c_incr :: CInt -> CInt
c_incr x = x + 1
