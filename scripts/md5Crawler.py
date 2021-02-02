# python2 script!
import hashlib
import glob
import argparse
import sys
import os



def stderr(text):
    sys.stderr.write("{}\n".format(text))


def arguments():
    ap = argparse.ArgumentParser(description='Read all files in a directory and calculate md5 hash')
    ap.add_argument('-d', '--directory', default = ".")  
    args = vars(ap.parse_args())
    return args
    

if __name__ == "__main__":

    args = arguments()
#    stderr(args)
    base = args['directory']
#    stderr(base)
    if not os.path.exists(base):
        stderr("Could not find path: %s"%(base))
        exit(1)

    all_files = glob.glob(base)
    for dirpath, dirs, files in os.walk(base):
        for filename in files:
	    fname = os.path.abspath(os.path.join(dirpath,filename))
            if not os.path.isdir(fname):
                try:
                    hash_res = hashlib.md5(open(fname,'rb').read()).hexdigest()
                    print('"{}","{}"'.format(fname, hash_res))
                except IOError:
                    stderr('File "{}" caused an IOError'.format(fname))


