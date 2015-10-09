var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var minifyCss = require('gulp-minify-css');
var rename = require('gulp-rename');
var del = require('del');

var paths = {
    static_dir: 'src/main/resources/static/',
    node_dir: 'node_modules/',
    scripts: [
        'node_modules/jquery/dist/jquery.min.js'
        , 'node_modules/jsencrypt/bin/jsencrypt.js'
        , 'src/main/resources/static/js/encryption.js'
    ],
    css: [
        'node_modules/bootstrap/dist/css/bootstrap.css'
    ]
};

gulp.task('clean', function () {
    return del([paths.static_dir + 'js/*.min.js', paths.static_dir + 'css']);
});

gulp.task('minifyCss', function () {
    return gulp.src(paths.css).pipe(minifyCss()).pipe(rename({suffix: '.min'})).pipe(gulp.dest(paths.static_dir + 'css'));
});

gulp.task('contactScripts', function () {
    return gulp.src(paths.scripts).pipe(uglify()).pipe(concat('main.min.js')).pipe(gulp.dest(paths.static_dir + 'js'));
});

gulp.task('default', ['clean', 'minifyCss', 'contactScripts'], function () {

});